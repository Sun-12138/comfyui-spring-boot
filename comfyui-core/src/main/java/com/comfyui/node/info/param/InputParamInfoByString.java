package com.comfyui.node.info.param;

/**
 * String类型的节点输入信息
 */
public class InputParamInfoByString extends InputParamInfo {


    /**
     * 创建类型为Boolean或String的参数信息
     *
     * @param inputName    参数名
     * @param inputType    输入参数类型 String或Boolean
     * @param defaultValue 默认值
     */
    public InputParamInfoByString(String inputName, InputParamType inputType, String defaultValue) {
        super(inputName, inputType, defaultValue);
    }

    /**
     * 获取默认值
     */
    @Override
    public String getDefaultValue() {
        return (String) super.defaultValue;
    }
}
