package com.comfyui.monitor.handler.strategy;

import com.comfyui.common.process.ComfyTaskError;
import com.comfyui.monitor.enums.ComfyWebSocketMessageType;
import com.comfyui.monitor.handler.TaskProcessContext;
import com.comfyui.monitor.message.TaskProcessSender;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 任务中断或取消
 * @author Sun_12138
 */
@Component
@RequiredArgsConstructor
public class TaskInterruptedHandleStrategy implements IComfyWebSocketTextHandleStrategy {

    /**
     * 处理消息
     *
     * @param msgType       消息类型
     * @param dataNode      消息内容
     * @param processSender 消息进度发送者
     * @param ctx           上下文任务状态
     */
    @Override
    public void handleMessage(ComfyWebSocketMessageType msgType, JsonNode dataNode, TaskProcessSender processSender, TaskProcessContext ctx) {
        //关闭ws客户端的监听状态
        ctx.getWebsocketClient().stopDrawingTask();
        processSender.taskError(new ComfyTaskError(ctx.getTaskId(), ctx.getComfyTaskId(), "任务被取消"));
    }
}
