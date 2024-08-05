package com.comfyui.node.info.param;

import java.util.List;

/**
 * Select选择器类型的节点输入信息
 * @author Sun_12138
 */
public class InputParamInfoBySelect extends AbstractInputParamInfo {
    /**
     * 创建类型为Select的参数信息
     *
     * @param inputName  参数名
     * @param selectList 选择列表
     */
    public InputParamInfoBySelect(String inputName, List<String> selectList) {
        super(inputName, selectList);
    }

    /**
     * 获取待选参数列表
     */
    @Override
    public List<String> getSelectList() {
        return super.selectList;
    }
}
