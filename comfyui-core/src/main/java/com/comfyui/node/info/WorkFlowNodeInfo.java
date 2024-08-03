package com.comfyui.node.info;

import com.comfyui.node.info.param.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流节点信息
 */
@Data
@JsonDeserialize(using = WorkFlowNodeInfo.Deserializer.class)
public class WorkFlowNodeInfo {

    /**
     * 输入参数 key为参数名
     */
    private Map<String, InputParamInfo> inputParam;

    /**
     * 接收端点信息
     */
    private List<EndpointInfo> inputNode;

    /**
     * 输出端点信息
     */
    private List<EndpointInfo> outputEndpoint;

    /**
     * <code>alias: </code>name<br>
     * 节点类名
     */
    private String nodeClass;

    /**
     * <code>alias: </code>display_name
     * 在网页ui上显示的名字
     */
    private String nodeName;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 节点分组
     */
    private String category;

    /**
     * 是否为输出节点 比如用于保存图片的节点等
     */
    private boolean outputNode;

    /**
     * {@link WorkFlowNodeInfo}反序列化类
     */
    protected static class Deserializer extends JsonDeserializer<WorkFlowNodeInfo> {

        private WorkFlowNodeInfo targetNodeInfo;

        @Override
        public WorkFlowNodeInfo deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode root = p.readValueAsTree();
            targetNodeInfo = new WorkFlowNodeInfo();
            JsonNode inputNode = root.get("input").get("required");
            //处理input
            deserializeInput(inputNode);
            //处理output
            List<EndpointInfo> outputEndpoint = new ArrayList<>();
            ArrayNode outputNodeList = (ArrayNode) root.get("output");
            ArrayNode outputNameList = (ArrayNode) root.get("output_name");
            for (int i = 0; i < outputNodeList.size(); i++) {
                String endpointType = outputNodeList.get(i).asText();
                String outputName = outputNameList.get(i).asText();
                outputEndpoint.add(new EndpointInfo(outputName, endpointType));
            }
            targetNodeInfo.outputEndpoint = outputEndpoint;
            //处理剩余信息
            String nodeClass = root.get("name").asText();
            String nodeName = root.get("display_name").asText();
            String description = root.get("description").asText();
            String category = root.get("category").asText();
            boolean outputNode = root.get("output_node").asBoolean();
            targetNodeInfo.nodeClass = nodeClass;
            targetNodeInfo.nodeName = nodeName;
            targetNodeInfo.description = description;
            targetNodeInfo.category = category;
            targetNodeInfo.outputNode = outputNode;
            return targetNodeInfo;
        }

        /**
         * 将node转为{@link InputParamInfo}对象 key为参数名
         *
         * @param node 需要转换的节点
         */
        private void deserializeInput(JsonNode node) {
            //参数输入
            Map<String, InputParamInfo> resultInputs = new HashMap<>();
            //节点输入
            List<EndpointInfo> inputNode = new ArrayList<>();
            if (node != null) {
                node.fieldNames().forEachRemaining(paramName -> {
                    InputParamInfo infoObj = null;
                    ArrayNode infoListNode = (ArrayNode) node.get(paramName);
                    if (infoListNode.size() == 1) {
                        //当前为Select类型或节点输入
                        JsonNode oneNode = infoListNode.get(0);
                        if (oneNode.isArray()) {
                            //为Select类型
                            List<String> selectList = new ArrayList<>();
                            oneNode.forEach(i -> selectList.add(i.asText()));
                            infoObj = new InputParamInfoBySelect(paramName, selectList);
                            resultInputs.put(paramName, infoObj);
                        } else if (oneNode.isTextual()) {
                            //为节点输入
                            inputNode.add(new EndpointInfo(paramName, oneNode.asText()));
                        }
                        return;
                    }
                    JsonNode paramInfoNode = infoListNode.get(1);
                    //判断参数类型
                    switch (infoListNode.get(0).asText()) {
                        case "INT":
                            int defaultValueByInt = paramInfoNode.get("default").asInt();
                            int maxValueByInt = paramInfoNode.has("max") ? paramInfoNode.get("max").asInt() : Integer.MAX_VALUE;
                            int minValueByInt = paramInfoNode.has("min") ? paramInfoNode.get("min").asInt() : Integer.MIN_VALUE;
                            int stepCountByInt = paramInfoNode.has("step") ? paramInfoNode.get("step").asInt() : -1;
                            infoObj = new InputParamInfoByInt(paramName, InputParamType.Int, defaultValueByInt, maxValueByInt, minValueByInt, stepCountByInt);
                            break;
                        case "FLOAT":
                            float defaultValueByFloat = (float) paramInfoNode.get("default").asDouble();
                            float maxValueByFloat = paramInfoNode.has("max") ? (float) paramInfoNode.get("max").asDouble() : Float.MAX_VALUE;
                            float minValueByFloat = paramInfoNode.has("min") ? (float) paramInfoNode.get("min").asDouble() : Float.MIN_VALUE;
                            float stepCountByFloat = paramInfoNode.has("step") ? (float) paramInfoNode.get("step").asDouble() : -1f;
                            infoObj = new InputParamInfoByFloat(paramName, InputParamType.Float, defaultValueByFloat, maxValueByFloat, minValueByFloat, stepCountByFloat);
                            break;
                        case "STRING":
                            String defaultValueByStr = paramInfoNode.has("default") ? paramInfoNode.get("default").asText() : "";
                            infoObj = new InputParamInfoByString(paramName, InputParamType.String, defaultValueByStr);
                            break;
                        case "BOOLEAN":
                            Boolean defaultValueByBool = paramInfoNode.has("default") ? paramInfoNode.get("default").asBoolean() : Boolean.FALSE;
                            infoObj = new InputParamInfoByBoolean(paramName, InputParamType.Boolean, defaultValueByBool);
                            break;
                    }
                    resultInputs.put(paramName, infoObj);
                });
            }
            targetNodeInfo.inputParam = resultInputs;
            targetNodeInfo.inputNode = inputNode;
        }
    }

}
