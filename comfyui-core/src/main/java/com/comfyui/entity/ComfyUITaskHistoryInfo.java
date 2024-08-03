package com.comfyui.entity;

import com.comfyui.node.ComfyUIWorkFlow;
import com.comfyui.utils.JsonUtils;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 任务信息
 */
@Getter
@AllArgsConstructor
@JsonDeserialize(using = ComfyUITaskHistoryInfo.Deserializer.class)
public class ComfyUITaskHistoryInfo {

    /**
     * comfyUI内部任务id
     */
    private String comfyUITaskId;

    /**
     * 工作流参数
     */
    private ComfyUIWorkFlow flow;

    /**
     * 任务输出的图片
     */
    private List<ComfyUITaskImage> outputs;

    /**
     * 任务是否完成
     */
    private Boolean completed;

    /**
     * 自定义WorkFLowData Json解析器
     */
    static class Deserializer extends JsonDeserializer<ComfyUITaskHistoryInfo> {

        @Override
        public ComfyUITaskHistoryInfo deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode root = p.readValueAsTree();
            //处理promptId
            JsonNode promptIdNode = root.get("prompt").get(1);
            String promptId = promptIdNode.asText();

            //处理prompt
            JsonNode promptNode = root.get("prompt").get(2);
            ComfyUIWorkFlow prompt = JsonUtils.toObject(promptNode, ComfyUIWorkFlow.class);
            //处理outputs
            JsonNode outputsNode = root.get("outputs");
            List<ComfyUITaskImage> imageList = new ArrayList<>();
            Iterator<String> it = outputsNode.fieldNames();
            while (it.hasNext()) {
                String key = it.next();
                JsonNode images = outputsNode.get(key).get("images");
                for (JsonNode image : images) {
                    ComfyUITaskImage taskImage = JsonUtils.toObject(image, ComfyUITaskImage.class);
                    imageList.add(taskImage);
                }
            }

            //处理status
            Boolean status = root.get("status").get("completed").asBoolean();
            return new ComfyUITaskHistoryInfo(promptId, prompt, imageList, status);
        }
    }
}
