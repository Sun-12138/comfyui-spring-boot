package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

/**
 * 任务失败
 *
 * @param taskId        自定义任务id
 * @param comfyUITaskId comfyUI内部任务id
 * @param errorInfo     错误信息
 */
public record ComfyUITaskError(String taskId, String comfyUITaskId, String errorInfo) implements IComfyUITaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
