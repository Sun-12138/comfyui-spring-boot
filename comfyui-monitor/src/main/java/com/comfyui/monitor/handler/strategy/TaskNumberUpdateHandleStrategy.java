package com.comfyui.monitor.handler.strategy;

import com.comfyui.common.process.ComfyTaskNumber;
import com.comfyui.monitor.enums.ComfyWebSocketMessageType;
import com.comfyui.monitor.handler.TaskProcessContext;
import com.comfyui.monitor.message.TaskProcessSender;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * 队列任务数量更新
 * @author Sun_12138
 */
@Component
public class TaskNumberUpdateHandleStrategy implements IComfyWebSocketTextHandleStrategy {

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
        int taskNumber = dataNode.get("status").get("exec_info").get("queue_remaining").asInt();
        processSender.taskNumberUpdate(new ComfyTaskNumber(taskNumber));
    }
}
