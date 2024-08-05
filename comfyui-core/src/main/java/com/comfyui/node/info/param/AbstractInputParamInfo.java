package com.comfyui.node.info.param;

import lombok.Getter;

import java.util.List;

/**
 * 节点输入信息
 * @author Sun_12138
 */
public abstract class AbstractInputParamInfo {

    /**
     * 输入参数名
     */
    @Getter
    private final String inputName;

    /**
     * 输入参数类型
     */
    @Getter
    private final InputParamType inputType;

    /**
     * 默认值
     */
    protected Object defaultValue;

    //当inputType为Int或Float时会有的属性
    /**
     * 最大值
     */
    protected Number maxValue;
    /**
     * 最小值
     */
    protected Number minValue;
    /**
     * 步长
     */
    protected Number stepCount;

    /**
     * 当inputType为Select时会有的属性 待选参数列表
     */
    protected List<String> selectList;

    /**
     * 创建类型为Select的参数信息
     *
     * @param inputName 参数名
     * @param selectList 选择列表
     */
    public AbstractInputParamInfo(String inputName, List<String> selectList) {
        this.inputName = inputName;
        this.inputType = InputParamType.Select;
        this.selectList = selectList;
    }

    /**
     * 创建类型为Boolean或String的参数信息
     *
     * @param inputName 参数名
     * @param inputType 输入参数类型 String或Boolean
     * @param defaultValue 默认值
     */
    public AbstractInputParamInfo(String inputName, InputParamType inputType, Object defaultValue) {
        if (inputType != InputParamType.String && inputType != InputParamType.Boolean) {
            throw new IllegalArgumentException("Input type must be either String or Boolean");
        }
        this.inputName = inputName;
        this.inputType = inputType;
        this.defaultValue = defaultValue;
    }

    /**
     * 创建类型为Int或Float的参数信息
     *
     * @param inputName 参数名
     * @param inputType 输入参数类型 Int或Float
     * @param defaultValue 默认值
     * @param maxValue 最大值
     * @param minValue 最小值
     * @param stepCount 步长
     */
    public AbstractInputParamInfo(String inputName, InputParamType inputType, Object defaultValue, Number maxValue, Number minValue, Number stepCount) {
        if (inputType != InputParamType.Float && inputType != InputParamType.Int) {
            throw new IllegalArgumentException("Input type must be either Float or Int");
        }
        this.inputName = inputName;
        this.inputType = inputType;
        this.defaultValue = defaultValue;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.stepCount = stepCount;
    }

    /**
     * 获取默认值
     */
    protected Object getDefaultValue() {
        throw new IllegalStateException("method not supported");
    }

    /**
     * 获取最大值
     */
    protected Number getMaxValue() {
        throw new IllegalStateException("method not supported");
    }

    /**
     * 获取最小值
     */
    protected Number getMinValue() {
        throw new IllegalStateException("method not supported");
    }

    /**
     * 获取步长
     */
    protected Number getStepCount() {
        throw new IllegalStateException("method not supported");
    }

    /**
     * 获取待选参数列表
     */
    protected List<String> getSelectList() {
        throw new IllegalStateException("method not supported");
    }
}
