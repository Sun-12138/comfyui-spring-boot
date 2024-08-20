package com.comfyui.monitor.handler.strategy;

import com.comfyui.common.process.ComfyTaskStart;
import com.comfyui.monitor.enums.ComfyWebSocketMessageType;
import com.comfyui.monitor.handler.TaskProcessContext;
import com.comfyui.monitor.message.TaskProcessSender;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * 任务开始
 * @author Sun_12138
 */
@Component
public class TaskStartHandleStrategy implements IComfyWebSocketTextHandleStrategy {

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
        processSender.taskStart(new ComfyTaskStart(ctx.getTaskId(), ctx.getComfyTaskId()));
    }
}
