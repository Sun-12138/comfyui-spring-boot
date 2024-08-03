package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

/**
 * @param taskId        任务id
 * @param comfyUITaskId comfyUI内部任务id
 * @param data          图片二进制数组
 */
public record ComfyUITaskProgressPreview(String taskId, String comfyUITaskId,
                                         byte[] data) implements IComfyUITaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
