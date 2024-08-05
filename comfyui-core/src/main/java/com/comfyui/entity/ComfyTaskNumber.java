package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

/**
 * 绘图队列任务个数更新
 *
 * @author Sun_12138
 * @param newTaskNumber 新的任务个数
 */
public record ComfyTaskNumber(int newTaskNumber) implements IComfyTaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
