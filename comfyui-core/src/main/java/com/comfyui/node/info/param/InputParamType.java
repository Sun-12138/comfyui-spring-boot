package com.comfyui.node.info.param;

import lombok.Getter;

/**
 * 工作流节点输入参数类型
 *
 * @author Sun_12138
 */
@Getter
public enum InputParamType {
    /**
     * 字符串
     */
    String,
    /**
     * 整数
     */
    Int,
    /**
     * 浮点数
     */
    Float,
    /**
     * 布尔值
     */
    Boolean,
    //以下为自定义类型
    /**
     * 选择列表
     */
    Select
}
