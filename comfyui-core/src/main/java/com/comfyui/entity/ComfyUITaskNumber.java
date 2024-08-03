package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

/**
 * 绘图队列任务个数更新
 *
 * @param newTaskNumber 新的任务个数
 */
public record ComfyUITaskNumber(int newTaskNumber) implements IComfyUITaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
