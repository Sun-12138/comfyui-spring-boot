package com.comfyui.client.handler.strategy;

import com.comfyui.client.ComfyUIWebSocketClient;
import com.comfyui.client.enums.ComfyUITaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfyUITaskError;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.*;
import org.springframework.stereotype.Component;

/**
 * 任务中断或取消
 */
@Component
@RequiredArgsConstructor
public class TaskInterruptedHandleStrategy implements IComfyUIWebSocketTextHandleStrategy {

    private final ApplicationContext applicationContext;

    //解决循环依赖
    private ComfyUIWebSocketClient getWebSocketClientBean() {
        return applicationContext.getBean(ComfyUIWebSocketClient.class);
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
    public void handleMessage(ComfyUITaskMsgType msgType, JsonNode dataNode, TaskProcessSender processSender, TaskHandlerStrategyContext ctx) {
        //关闭ws客户端的监听状态
        getWebSocketClientBean().stopDrawingTask();
        processSender.taskError(new ComfyUITaskError(ctx.taskId(), ctx.comfyUITaskId(), "任务被取消"));
    }
}
