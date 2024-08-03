package com.comfyui.node.info.param;

/**
 * Float类型的节点输入信息
 */
public class InputParamInfoByFloat extends InputParamInfo {

    /**
     * 创建类型为Int或Float的参数信息
     *
     * @param inputName    参数名
     * @param inputType    输入参数类型 Int或Float
     * @param defaultValue 默认值
     * @param maxValue     最大值
     * @param minValue     最小值
     * @param stepCount    步长
     */
    public InputParamInfoByFloat(String inputName, InputParamType inputType, Float defaultValue, Float maxValue, Float minValue, Float stepCount) {
        super(inputName, inputType, defaultValue, maxValue, minValue, stepCount);
    }

    /**
     * 获取默认值
     */
    @Override
    public Float getDefaultValue() {
        return (Float) super.defaultValue;
    }

    /**
     * 获取最大值
     */
    @Override
    public Float getMaxValue() {
        return super.maxValue.floatValue();
    }

    /**
     * 获取最小值
     */
    @Override
    public Float getMinValue() {
        return super.minValue.floatValue();
    }

    /**
     * 获取步长
     */
    @Override
    public Float getStepCount() {
        return super.stepCount.floatValue();
    }
}
