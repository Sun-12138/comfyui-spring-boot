package com.comfyui.entity;


import com.comfyui.utils.JsonUtils;

/**
 * 任务开始
 *
 * @author Sun_12138
 * @param taskId        自定义任务id
 * @param comfyTaskId comfyUI内部任务id
 */
public record ComfyTaskStart(String taskId, String comfyTaskId) implements IComfyTaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
