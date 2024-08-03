package com.comfyui.client.handler.strategy;

import com.comfyui.client.enums.ComfyUITaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfyUITaskNumber;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class TaskNumberUpdateHandleStrategy implements IComfyUIWebSocketTextHandleStrategy {
    /**
     * 处理消息
     *
     * @param msgType       消息类型
     * @param dataNode      消息内容
     * @param processSender 任务处理结果接收对象
     * @param ctx           上下文任务状态
     */
    @Override
    public void handleMessage(ComfyUITaskMsgType msgType, JsonNode dataNode, TaskProcessSender processSender, TaskHandlerStrategyContext ctx) {
        int taskNumber = dataNode.get("status").get("exec_info").get("queue_remaining").asInt();
        processSender.taskNumberUpdate(new ComfyUITaskNumber(taskNumber));
    }
}
