package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

import java.util.List;

/**
 * 任务输出的图片
 *
 * @param taskId        自定义任务id
 * @param comfyUITaskId comfyUI内部任务id
 * @param outputImages  输出的图片
 */
public record ComfyUITaskOutput(String taskId, String comfyUITaskId,
                                List<ComfyUITaskImage> outputImages) implements IComfyUITaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
