package com.comfyui.client.handler.strategy;

import com.comfyui.client.enums.ComfyTaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfyTaskStart;
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
     * @param processSender 任务处理结果接收对象
     * @param ctx           上下文任务状态
     */
    @Override
    public void handleMessage(ComfyTaskMsgType msgType, JsonNode dataNode, TaskProcessSender processSender, TaskHandlerStrategyContext ctx) {
        processSender.taskStart(new ComfyTaskStart(ctx.taskId(), ctx.comfyTaskId()));
    }
}
