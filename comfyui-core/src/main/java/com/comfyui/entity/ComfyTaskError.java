package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

/**
 * 任务失败
 *
 * @author Sun_12138
 * @param taskId        自定义任务id
 * @param comfyTaskId comfyUI内部任务id
 * @param errorInfo     错误信息
 */
public record ComfyTaskError(String taskId, String comfyTaskId, String errorInfo) implements IComfyTaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
