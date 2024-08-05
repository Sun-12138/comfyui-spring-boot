package com.comfyui.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 绘图任务队列状态
 * @author Sun_12138
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = ComfyTaskQueueStatus.Deserializer.class)
public class ComfyTaskQueueStatus implements IComfyTaskProcess {
    /**
     * 正在运行的任务
     */
    private List<String> running;
    /**
     * 正在等待的任务
     */
    private List<String> pending;

    static class Deserializer extends JsonDeserializer<ComfyTaskQueueStatus> {

        @Override
        public ComfyTaskQueueStatus deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode root = p.readValueAsTree();

            //处理queue_running
            List<String> runningTaskIdList = new ArrayList<>();
            JsonNode runningNode = root.get("queue_running");
            for (JsonNode taskNode : runningNode) {
                String taskId = taskNode.get(1).asText();
                runningTaskIdList.add(taskId);
            }

            //处理queue_pending
            List<String> pendingTaskIdList = new ArrayList<>();
            JsonNode pendingNode = root.get("queue_pending");
            for (JsonNode taskNode : pendingNode) {
                String taskId = taskNode.get(1).asText();
                pendingTaskIdList.add(taskId);
            }
            return new ComfyTaskQueueStatus(runningTaskIdList, pendingTaskIdList);
        }
    }

}
