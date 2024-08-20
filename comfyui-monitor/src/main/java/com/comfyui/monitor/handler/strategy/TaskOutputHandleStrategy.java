package com.comfyui.monitor.handler.strategy;

import com.comfyui.common.utils.JsonUtils;
import com.comfyui.common.entity.ComfyTaskImage;
import com.comfyui.common.process.ComfyTaskOutput;
import com.comfyui.monitor.enums.ComfyWebSocketMessageType;
import com.comfyui.monitor.handler.TaskProcessContext;
import com.comfyui.monitor.message.TaskProcessSender;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务输出的图片
 * @author Sun_12138
 */
@Component
public class TaskOutputHandleStrategy implements IComfyWebSocketTextHandleStrategy {

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
        String nodeId = dataNode.get("node").asText();
        JsonNode imagesNode = dataNode.get("output").get("images");
        //获取上下文输出的图片信息
        List<ComfyTaskImage> outputImagesContext = ctx.getOutputImages();
        List<ComfyTaskImage> currentOutputImages = new ArrayList<>();
        for (JsonNode imageNode : imagesNode) {
            ComfyTaskImage imageInfo = JsonUtils.toObject(imageNode, ComfyTaskImage.class);
            currentOutputImages.add(imageInfo);
        }
        outputImagesContext.addAll(currentOutputImages);
        processSender.taskOutput(new ComfyTaskOutput(ctx.getTaskId(), ctx.getComfyTaskId(), currentOutputImages, ctx.getWorkFlowNode(nodeId)));
    }
}
