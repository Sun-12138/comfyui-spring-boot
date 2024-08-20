package com.comfyui.monitor.handler;

import com.comfyui.monitor.enums.ComfyWebSocketMessageType;
import com.comfyui.monitor.handler.strategy.IComfyWebSocketByteHandleStrategy;
import com.comfyui.monitor.handler.strategy.IComfyWebSocketTextHandleStrategy;
import com.comfyui.monitor.message.TaskProcessSender;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于映射进度消息类型与对应的处理策略类
 *
 * @author Sun_12138
 */
@Slf4j
public class ComfyWebSocketMessageHandler {

    /**
     * 任务进度信息处理策略
     * key为 消息类型type value对应任务处理策略实现类
     */
    private final Map<ComfyWebSocketMessageType, IComfyWebSocketTextHandleStrategy> msgStrategyMapper = new HashMap<>();

    /**
     * 任务进度预览图处理策略
     */
    private final List<IComfyWebSocketByteHandleStrategy> byteStrategyMapper = new ArrayList<>();

    /**
     * 消息进度发送者
     */
    private final TaskProcessSender processSender;

    /**
     * @param processSender 消息进度发送者
     * @param textStrategy  任务进度信息处理策略
     * @param byteStrategy  任务进度预览图处理策略
     */
    public ComfyWebSocketMessageHandler(
            TaskProcessSender processSender,
            Map<ComfyWebSocketMessageType, IComfyWebSocketTextHandleStrategy> textStrategy,
            List<IComfyWebSocketByteHandleStrategy> byteStrategy
    ) {
        this.processSender = processSender;
        this.msgStrategyMapper.putAll(textStrategy);
        this.byteStrategyMapper.addAll(byteStrategy);
    }

    /**
     * 处理消息
     *
     * @param msgType  消息类型
     * @param dataNode 消息内容
     * @param context  上下文任务状态
     */
    public void handleMessage(ComfyWebSocketMessageType msgType, JsonNode dataNode, TaskProcessContext context) {
        //获取对应的策略实现类
        IComfyWebSocketTextHandleStrategy strategy = msgStrategyMapper.get(msgType);
        if (strategy == null) {
            log.error("Message processing strategy with message type: {} not found", msgType);
            return;
        }
        strategy.handleMessage(msgType, dataNode, processSender, context);
    }

    /**
     * 处理二进制消息
     *
     * @param bytes   二进制数组
     * @param context 上下文任务状态
     */
    public void handleByte(byte[] bytes, TaskProcessContext context) {
        byteStrategyMapper.forEach(action -> action.handleByte(bytes, processSender, context));
    }
}
