package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

import java.util.List;

/**
 * 任务完成
 *
 * @param taskId        自定义任务id
 * @param comfyUITaskId comfyUI内部任务id
 * @param images        任务输出的图片
 * @param isCache       是否为缓存结果
 */
public record ComfyUITaskComplete(String taskId, String comfyUITaskId, List<ComfyUITaskImage> images,
                                  boolean isCache) implements IComfyUITaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
