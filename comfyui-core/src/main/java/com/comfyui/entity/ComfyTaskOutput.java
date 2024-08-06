package com.comfyui.entity;

import com.comfyui.node.ComfyWorkFlowNode;
import com.comfyui.utils.JsonUtils;

import java.util.List;

/**
 * 任务输出的图片
 *
 * @author Sun_12138
 * @param taskId        自定义任务id
 * @param comfyTaskId comfyUI内部任务id
 * @param outputImages  输出的图片
 */
public record ComfyTaskOutput(String taskId, String comfyTaskId,
                              List<ComfyTaskImage> outputImages, ComfyWorkFlowNode node) implements IComfyTaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
