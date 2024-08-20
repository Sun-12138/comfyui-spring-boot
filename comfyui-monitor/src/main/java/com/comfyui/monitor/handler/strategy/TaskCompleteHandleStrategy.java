package com.comfyui.monitor.handler.strategy;

import com.comfyui.api.ComfyApiClient;
import com.comfyui.common.entity.ComfyTaskHistoryInfo;
import com.comfyui.common.entity.ComfyTaskImage;
import com.comfyui.common.process.ComfyTaskComplete;
import com.comfyui.monitor.enums.ComfyWebSocketMessageType;
import com.comfyui.monitor.handler.TaskProcessContext;
import com.comfyui.monitor.message.TaskProcessSender;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务完成
 * @author Sun_12138
 */
@Component
@RequiredArgsConstructor
public class TaskCompleteHandleStrategy implements IComfyWebSocketTextHandleStrategy {

    /**
     * ComfyUI客户端
     */
    private final ComfyApiClient comfyClient;

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
        List<ComfyTaskImage> out = ctx.getOutputImages();
        boolean isCache = false;
        if (out.isEmpty()) {
            //当前任务为缓存任务时 则请求历史任务接口获取输出的图片
            isCache = true;
            ComfyTaskHistoryInfo info = comfyClient.getTaskInfoById(ctx.getComfyTaskId());
            out.addAll(info.getOutputs());
        }
        //任务完成 关闭ws客户端的监听状态
        ctx.getWebsocketClient().stopDrawingTask();
        processSender.taskComplete(new ComfyTaskComplete(ctx.getTaskId(), ctx.getComfyTaskId(), out, isCache));
    }
}
