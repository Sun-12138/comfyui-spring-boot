package com.comfyui.node.info.param;

/**
 * Boolean类型的节点输入信息
 */
public class InputParamInfoByBoolean extends InputParamInfo {


    /**
     * 创建类型为Boolean或String的参数信息
     *
     * @param inputName    参数名
     * @param inputType    输入参数类型 String或Boolean
     * @param defaultValue 默认值
     */
    public InputParamInfoByBoolean(String inputName, InputParamType inputType, Boolean defaultValue) {
        super(inputName, inputType, defaultValue);
    }

    /**
     * 获取默认值
     */
    @Override
    public Boolean getDefaultValue() {
        return (Boolean) super.defaultValue;
    }
}
