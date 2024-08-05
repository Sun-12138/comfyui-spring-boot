package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

import java.util.List;

/**
 * 任务完成
 *
 * @author Sun_12138
 * @param taskId        自定义任务id
 * @param comfyTaskId comfyUI内部任务id
 * @param images        任务输出的图片
 * @param cache       是否为缓存结果
 */
public record ComfyTaskComplete(String taskId, String comfyTaskId, List<ComfyTaskImage> images,
                                boolean cache) implements IComfyTaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
