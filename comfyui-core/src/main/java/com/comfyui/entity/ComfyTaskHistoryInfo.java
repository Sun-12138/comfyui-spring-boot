package com.comfyui.entity;

import com.comfyui.node.ComfyWorkFlow;
import com.comfyui.utils.JsonUtils;
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
 * @author Sun_12138
 */
@Getter
@AllArgsConstructor
@JsonDeserialize(using = ComfyTaskHistoryInfo.Deserializer.class)
public class ComfyTaskHistoryInfo {

    /**
     * comfyUI内部任务id
     */
    private String comfyTaskId;

    /**
     * 工作流参数
     */
    private ComfyWorkFlow flow;

    /**
     * 任务输出的图片
     */
    private List<ComfyTaskImage> outputs;

    /**
     * 任务是否完成
     */
    private Boolean completed;

    /**
     * 自定义WorkFLowData Json解析器
     */
    static class Deserializer extends JsonDeserializer<ComfyTaskHistoryInfo> {

        @Override
        public ComfyTaskHistoryInfo deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode root = p.readValueAsTree();
            //处理promptId
            JsonNode promptIdNode = root.get("prompt").get(1);
            String promptId = promptIdNode.asText();

            //处理prompt
            JsonNode promptNode = root.get("prompt").get(2);
            ComfyWorkFlow prompt = JsonUtils.toObject(promptNode, ComfyWorkFlow.class);
            //处理outputs
            JsonNode outputsNode = root.get("outputs");
            List<ComfyTaskImage> imageList = new ArrayList<>();
            Iterator<String> it = outputsNode.fieldNames();
            while (it.hasNext()) {
                String key = it.next();
                JsonNode images = outputsNode.get(key).get("images");
                for (JsonNode image : images) {
                    ComfyTaskImage taskImage = JsonUtils.toObject(image, ComfyTaskImage.class);
                    imageList.add(taskImage);
                }
            }

            //处理status
            Boolean status = root.get("status").get("completed").asBoolean();
            return new ComfyTaskHistoryInfo(promptId, prompt, imageList, status);
        }
    }
}
