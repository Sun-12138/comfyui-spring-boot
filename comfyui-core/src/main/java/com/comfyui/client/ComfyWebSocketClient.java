package com.comfyui.client;

import com.comfyui.TaskErrorException;
import com.comfyui.node.ComfyWorkFlow;
import com.comfyui.client.enums.ComfyTaskMsgType;
import com.comfyui.client.handler.strategy.ComfyWebSocketHandleStrategyMapper;
import com.comfyui.client.handler.strategy.TaskHandlerStrategyContext;
import com.comfyui.entity.ComfyTaskQueueStatus;
import com.comfyui.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sun_12138
 */
@Slf4j
public class ComfyWebSocketClient extends WebSocketClient {

    /**
     * 当前重连次数
     */
    private int reconnectAttempts = 0;

    /**
     * 是否进入准备监听状态
     */
    private boolean prepare = false;

    /**
     * 是否为监听状态
     */
    private boolean monitor = false;

    /**
     * 监听状态锁
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 策略模式上下文对象
     */
    private TaskHandlerStrategyContext taskContext = null;

    /**
     * 消息处理策略映射类
     */
    private final ComfyWebSocketHandleStrategyMapper strategyMapper;

    /**
     * ComfyUI客户端
     */
    private final ComfyClient comfyClient;


    /**
     * @param serverUri comfyUI websocket服务地址
     */
    public ComfyWebSocketClient(URI serverUri, ComfyClient comfyClient, ComfyWebSocketHandleStrategyMapper strategyMapper) {
        super(serverUri);
        this.comfyClient = comfyClient;
        this.strategyMapper = strategyMapper;
        this.connect();
    }

    /**
     * 提交绘图任务
     *
     * @param taskId 任务id
     * @param flow   工作流
     * @return ComfyUI内部任务id
     */
    public String startDrawingTask(String taskId, ComfyWorkFlow flow) {
        lock.lock();
        if (prepare) {
            throw new TaskErrorException(taskId, "绘图任务提交失败, 当前已有正在监听的任务");
        }
        //判断当前状态是否为准备状态
        this.prepare = true;
        //清除ComfyUI内部队列任务
        this.clearComfyTaskQueue();
        //提交绘图任务 获得comfyUI内部任务id
        try {
            String comfyTaskId = comfyClient.submitDrawTask(taskId, flow);
            this.taskContext = new TaskHandlerStrategyContext(taskId, comfyTaskId, flow, new ArrayList<>());
            return comfyTaskId;
        } catch (Exception e) {
            //发生错误则停止监听
            this.stopDrawingTask();
            throw e;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 停止监听任务
     */
    public void stopDrawingTask() {
        lock.lock();
        //重置任务监听状态
        this.prepare = false;
        this.monitor = false;
        //清空当前任务信息
        this.taskContext = null;
        lock.unlock();
    }

    /**
     * 清除ComfyUI内部队列任务
     */
    private void clearComfyTaskQueue() {
        ComfyTaskQueueStatus queueStatus = comfyClient.getQueueStatus();
        //当前队列等待的任务id
        List<String> pendingTaskId = queueStatus.getPending();
        //若没有任务则直接结束
        if (pendingTaskId.isEmpty()) {
            return;
        }
        pendingTaskId.forEach(comfyClient::cancelDrawTask);
        //取消当前运行的任务
        comfyClient.cancelRunningTask();
        //清除两次，为保证清除干净
        clearComfyTaskQueue();
    }

    /**
     * 字符串消息
     *
     * @param message 收到的 UTF-8 解码消息
     */
    @Override
    public void onMessage(String message) {
        try {
            //解析websocket消息
            JsonNode messageNode = JsonUtils.toJsonNode(message);
            JsonNode dataNode = messageNode.get("data");
            //获取消息类型
            ComfyTaskMsgType msgType = ComfyTaskMsgType.fromType(messageNode.get("type").asText());
            if (msgType == ComfyTaskMsgType.MONITOR || msgType == ComfyTaskMsgType.TASK_NUMBER) {
                //ComfyUI状态更新消息直接进行处理
                strategyMapper.handleMessage(msgType, dataNode, null);
                return;
            }
            //非准备状态不进行监听
            if (!prepare) {
                return;
            }
            //当前为非监听状态时，接收到有开始新任务的消息则修改为监听状态
            if (!monitor) {
                monitor = msgType == ComfyTaskMsgType.EXECUTION_START;
            }
            //当前状态为监听状态且当前任务为监听的任务id则处理该信息
            if (monitor && checkTask(msgType, dataNode)) {
                strategyMapper.handleMessage(msgType, dataNode, taskContext);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 检查当前任务是否是监听的任务
     *
     * @param msgType  任务消息类型
     * @param dataNode 任务消息对象
     */
    private boolean checkTask(ComfyTaskMsgType msgType, JsonNode dataNode) {
        //非任务进度消息返回true
        if (msgType == ComfyTaskMsgType.MONITOR || msgType == ComfyTaskMsgType.TASK_NUMBER) {
            return true;
        }
        return Objects.equals(dataNode.get("prompt_id").asText(), this.taskContext.comfyTaskId());
    }

    /**
     * 二进制消息
     *
     * @param bytes 收到的二进制消息
     */
    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            strategyMapper.handleByte(bytes.array(), taskContext);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info(String.format("ComfyUIWebSocketClient连接到%s", uri.toString()));
        // 重置重连尝试次数
        reconnectAttempts = 0;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (code == CloseFrame.NORMAL) {
            return;
        }
        //重试间隔时间 单位：毫秒
        long reconnectInterval = 5000;
        this.retryConnect(this, reconnectInterval);
    }

    @Override
    public void onError(Exception ex) {
    }

    /**
     * 重连websocket连接
     *
     * @param client  websocket连接对象
     * @param timeout 重试间隔 单位：毫秒
     */
    private void retryConnect(WebSocketClient client, long timeout) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            reconnectAttempts++;
            //最大重连尝试次数
            log.error("连接{}失败, 第{}尝试重新连接", uri, reconnectAttempts);
            try {
                //等待一段时间后再尝试重连
                Thread.sleep(timeout);
                client.reconnect();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
