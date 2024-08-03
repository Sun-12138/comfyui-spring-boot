package com.comfyui.client.handler.strategy;

import com.comfyui.client.enums.ComfyUITaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfyUITaskImage;
import com.comfyui.entity.ComfyUITaskOutput;
import com.comfyui.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务输出的图片
 */
@Component
public class TaskOutputHandleStrategy implements IComfyUIWebSocketTextHandleStrategy {
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
        JsonNode imagesNode = dataNode.get("output").get("images");
        //获取上下文输出的图片信息
        List<ComfyUITaskImage> outputImagesContext = ctx.outputImages();
        List<ComfyUITaskImage> currentOutputImages = new ArrayList<>();
        for (JsonNode imageNode : imagesNode) {
            ComfyUITaskImage imageInfo = JsonUtils.toObject(imageNode, ComfyUITaskImage.class);
            currentOutputImages.add(imageInfo);
        }
        outputImagesContext.addAll(currentOutputImages);
        processSender.taskOutput(new ComfyUITaskOutput(ctx.taskId(), ctx.comfyUITaskId(), currentOutputImages));
    }
}
