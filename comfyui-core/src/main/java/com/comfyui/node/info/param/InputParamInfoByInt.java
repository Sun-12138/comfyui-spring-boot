package com.comfyui.node.info.param;

/**
 * Int类型的节点输入信息
 */
public class InputParamInfoByInt extends InputParamInfo {

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
    public InputParamInfoByInt(String inputName, InputParamType inputType, Integer defaultValue, Integer maxValue, Integer minValue, Integer stepCount) {
        super(inputName, inputType, defaultValue, maxValue, minValue, stepCount);
    }

    /**
     * 获取默认值
     */
    @Override
    public Integer getDefaultValue() {
        return (Integer) super.defaultValue;
    }

    /**
     * 获取最大值
     */
    @Override
    public Integer getMaxValue() {
        return super.maxValue.intValue();
    }

    /**
     * 获取最小值
     */
    @Override
    public Integer getMinValue() {
        return super.minValue.intValue();
    }

    /**
     * 获取步长
     */
    @Override
    public Integer getStepCount() {
        return super.stepCount.intValue();
    }
}
