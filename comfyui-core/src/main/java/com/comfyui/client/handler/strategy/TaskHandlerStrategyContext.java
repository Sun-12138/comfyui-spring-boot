package com.comfyui.client.handler.strategy;

import com.comfyui.entity.ComfyTaskImage;

import java.util.List;

/**
 * @author Sun_12138
 * @param taskId        任务id
 * @param comfyTaskId comfyUI内部任务id
 * @param outputImages  输出的图片
 */
public record TaskHandlerStrategyContext(String taskId, String comfyTaskId, List<ComfyTaskImage> outputImages) {
}
