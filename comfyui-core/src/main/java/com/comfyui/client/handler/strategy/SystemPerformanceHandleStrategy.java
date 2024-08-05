package com.comfyui.client.handler.strategy;

import com.comfyui.client.enums.ComfyTaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfySystemPerformance;
import com.comfyui.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * 系统状态更新
 * @author Sun_12138
 */
@Component
public class SystemPerformanceHandleStrategy implements IComfyWebSocketTextHandleStrategy {
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
        ComfySystemPerformance performance = JsonUtils.toObject(dataNode, ComfySystemPerformance.class);
        processSender.systemPerformance(performance);
    }
}
