package com.comfyui.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * 工作流节点
 *
 * @param id          节点唯一标识符
 * @param title       节点标题名
 * @param classType   节点类型名
 * @param inputs 节点数值参数输入 key为参数名 value为输入值
 */
public record ComfyUIWorkFlowNode(@JsonIgnore Integer id, String title, @JsonProperty("class_type") String classType,
                                  Map<String, Object> inputs) {
}
