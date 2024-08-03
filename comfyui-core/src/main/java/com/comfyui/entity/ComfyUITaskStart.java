package com.comfyui.entity;


import com.comfyui.utils.JsonUtils;

/**
 * 任务开始
 *
 * @param taskId        自定义任务id
 * @param comfyUITaskId comfyUI内部任务id
 */
public record ComfyUITaskStart(String taskId, String comfyUITaskId) implements IComfyUITaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
