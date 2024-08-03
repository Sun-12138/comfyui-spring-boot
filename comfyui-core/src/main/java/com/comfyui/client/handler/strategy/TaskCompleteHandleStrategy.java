package com.comfyui.client.handler.strategy;

import com.comfyui.client.ComfyUIWebSocketClient;
import com.comfyui.client.enums.ComfyUITaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfyUITaskHistoryInfo;
import com.comfyui.entity.ComfyUITaskImage;
import com.comfyui.client.ComfyUIClient;
import com.comfyui.entity.ComfyUITaskComplete;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务完成
 */
@Component
@RequiredArgsConstructor
public class TaskCompleteHandleStrategy implements IComfyUIWebSocketTextHandleStrategy {

    private final ComfyUIClient comfyUIClient;

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
        List<ComfyUITaskImage> out = ctx.outputImages();
        boolean isCache = false;
        if (out.isEmpty()) {
            //当前任务为缓存任务时 则请求历史任务接口获取输出的图片
            isCache = true;
            ComfyUITaskHistoryInfo info = comfyUIClient.getTaskInfoById(ctx.comfyUITaskId());
            out.addAll(info.getOutputs());
        }
        //任务完成 关闭ws客户端的监听状态
        getWebSocketClientBean().stopDrawingTask();
        processSender.taskComplete(new ComfyUITaskComplete(ctx.taskId(), ctx.comfyUITaskId(), out, isCache));
    }
}
