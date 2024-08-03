package com.comfyui.node.info.param;

import lombok.Getter;

/**
 * 工作流节点输入参数类型
 */
@Getter
public enum InputParamType {
    String,
    Int,
    Float,
    Boolean,
    //以下为自定义类型
    Select;
}
