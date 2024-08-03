package com.comfyui.client.handler.strategy;

import com.comfyui.entity.ComfyUITaskImage;

import java.util.List;

/**
 * @param taskId        任务id
 * @param comfyUITaskId comfyUI内部任务id
 * @param outputImages  输出的图片
 */
public record TaskHandlerStrategyContext(String taskId, String comfyUITaskId, List<ComfyUITaskImage> outputImages) {
}
