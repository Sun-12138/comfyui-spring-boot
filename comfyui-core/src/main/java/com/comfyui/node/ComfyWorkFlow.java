package com.comfyui.node;

import com.comfyui.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作流对象
 * @author Sun_12138
 */
@JsonDeserialize(using = ComfyWorkFlow.Deserializer.class)
@JsonSerialize(using = ComfyWorkFlow.Serializer.class)
public class ComfyWorkFlow {

    /**
     * 工作流节点 key为节点id value为节点对象
     */
    protected final Map<String, ComfyWorkFlowNode> nodes;

    private ComfyWorkFlow(Map<String, ComfyWorkFlowNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * 创建ComfyUIWorkFlow对象
     *
     * @param json 工作流json字符串
     * @return ComfyUIWorkFlow
     */
    public static ComfyWorkFlow of(String json) {
        return JsonUtils.toObject(json, ComfyWorkFlow.class);
    }

    /**
     * 获得一个工作流节点
     *
     * @param id 节点id
     * @return 工作流节点
     */
    public ComfyWorkFlowNode getNode(int id) {
        return getNode(String.valueOf(id));
    }

    /**
     * 获得一个工作流节点
     *
     * @param id 节点id
     * @return 工作流节点
     */
    public ComfyWorkFlowNode getNode(String id) {
        return nodes.get(id);
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

    /**
     * {@link ComfyWorkFlow}反序列化类
     */
    protected static class Deserializer extends JsonDeserializer<ComfyWorkFlow> {
        /**
         * 工作流节点
         */
        private Map<String, ComfyWorkFlowNode> nodes;

        @Override
        public ComfyWorkFlow deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode root = p.readValueAsTree();
            nodes = new HashMap<>(root.size());
            root.fieldNames().forEachRemaining(field -> {
                Integer nodeId = Integer.valueOf(field);
                JsonNode nodeValue = root.get(field);
                ComfyWorkFlowNode newNode = deserializeWorkFlowNode(nodeId, nodeValue);
                nodes.put(field, newNode);
            });
            return new ComfyWorkFlow(nodes);
        }

        /**
         * 将node转为{@link ComfyWorkFlowNode}对象
         *
         * @param nodeId 节点id
         * @param node   需要转换的节点
         * @return {@link ComfyWorkFlowNode}对象
         */
        private ComfyWorkFlowNode deserializeWorkFlowNode(Integer nodeId, JsonNode node) {
            String title = node.get("_meta").get("title").asText();
            String classType = node.get("class_type").asText();
            //解析节点输出参数
            Map<String, Object> inputs = JsonUtils.toMapObject(node.get("inputs"), Object.class);
            return new ComfyWorkFlowNode(nodeId, title, classType, inputs);
        }
    }

    /**
     * {@link ComfyWorkFlow}序列化类
     */
    protected static class Serializer extends JsonSerializer<ComfyWorkFlow> {
        @Override
        public void serialize(ComfyWorkFlow comfyWorkFlow, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            //开始写入
            jsonGenerator.writeStartObject();
            for (Map.Entry<String, ComfyWorkFlowNode> item : comfyWorkFlow.nodes.entrySet()) {
                String nodeId = item.getKey();
                JsonNode valueNode = serializeWorkFlowNode(item.getValue());
                jsonGenerator.writeObjectField(nodeId, valueNode);
            }
            //结束写入
            jsonGenerator.writeEndObject();
        }

        /**
         * 序列化工作流节点对象
         *
         * @param comfyWorkFlowNode 工作流节点
         */
        public JsonNode serializeWorkFlowNode(ComfyWorkFlowNode comfyWorkFlowNode) {
            JsonNode resultNode = JsonUtils.toJsonNode(comfyWorkFlowNode);
            //修改title属性为_meta{title: value}
            ObjectNode resultObjectNode = (ObjectNode) resultNode;
            //创建titleNode {title: value}
            String titleValue = resultObjectNode.remove("title").asText();
            ObjectNode titleNode = JsonUtils.OBJECT_MAPPER.createObjectNode();
            titleNode.put("title", titleValue);
            //添加titleNode
            resultObjectNode.set("_meta", titleNode);
            return resultNode;
        }
    }
}
