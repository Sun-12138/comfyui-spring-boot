package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

/**
 * @author Sun_12138
 * @param taskId        任务id
 * @param comfyTaskId comfyUI内部任务id
 * @param data          图片二进制数组
 */
public record ComfyTaskProgressPreview(String taskId, String comfyTaskId,
                                       byte[] data) implements IComfyTaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
