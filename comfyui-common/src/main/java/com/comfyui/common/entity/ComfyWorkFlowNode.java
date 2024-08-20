package com.comfyui.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.Map;

/**
 * 工作流节点
 *
 * @author Sun_12138
 */
@Getter
public class ComfyWorkFlowNode {

    /**
     * 节点唯一标识符
     */
    @JsonIgnore
    private final Integer id;

    /**
     * 节点标题名
     */
    private final String title;

    /**
     * 节点类型名
     */
    @JsonProperty("class_type")
    private final String classType;

    /**
     * 节点数值参数输入 key为参数名 value为输入值
     */
    private final Map<String, Object> inputs;

    /**
     * @param id        节点唯一标识符
     * @param title     节点标题名
     * @param classType 节点类型名
     * @param inputs    节点数值参数输入 key为参数名 value为输入值
     */
    @ConstructorProperties({"id", "title", "class_type", "inputs"})
    public ComfyWorkFlowNode(Integer id, String title, String classType, Map<String, Object> inputs) {
        this.id = id;
        this.title = title;
        this.classType = classType;
        this.inputs = inputs;
    }
}
