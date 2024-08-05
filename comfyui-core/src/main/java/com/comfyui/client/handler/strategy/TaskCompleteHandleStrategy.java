package com.comfyui.client.handler.strategy;

import com.comfyui.client.ComfyWebSocketClient;
import com.comfyui.client.enums.ComfyTaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfyTaskHistoryInfo;
import com.comfyui.entity.ComfyTaskImage;
import com.comfyui.client.ComfyClient;
import com.comfyui.entity.ComfyTaskComplete;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务完成
 * @author Sun_12138
 */
@Component
@RequiredArgsConstructor
public class TaskCompleteHandleStrategy implements IComfyWebSocketTextHandleStrategy {

    private final ComfyClient comfyClient;

    private final ApplicationContext applicationContext;

    /**
     * 获得WebSocketClientBean(解决循环依赖)
     *
     * @return ComfyUIWebSocketClient
     */
    private ComfyWebSocketClient getWebSocketClientBean() {
        return applicationContext.getBean(ComfyWebSocketClient.class);
    }

    /**
     * 处理消息
     *
     * @param msgType       消息类型
     * @param dataNode      消息内容
     * @param processSender 任务处理结果接收对象
     * @param ctx           上下文任务状态
     */
    @Override
    public void handleMessage(ComfyTaskMsgType msgType, JsonNode dataNode, TaskProcessSender processSender, TaskHandlerStrategyContext ctx) {
        List<ComfyTaskImage> out = ctx.outputImages();
        boolean isCache = false;
        if (out.isEmpty()) {
            //当前任务为缓存任务时 则请求历史任务接口获取输出的图片
            isCache = true;
            ComfyTaskHistoryInfo info = comfyClient.getTaskInfoById(ctx.comfyTaskId());
            out.addAll(info.getOutputs());
        }
        //任务完成 关闭ws客户端的监听状态
        getWebSocketClientBean().stopDrawingTask();
        processSender.taskComplete(new ComfyTaskComplete(ctx.taskId(), ctx.comfyTaskId(), out, isCache));
    }
}
