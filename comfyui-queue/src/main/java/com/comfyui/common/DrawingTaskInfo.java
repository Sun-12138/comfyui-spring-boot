package com.comfyui.common;

import com.comfyui.node.ComfyUIWorkFlow;

import java.util.concurrent.TimeUnit;

/**
 * 绘图任务信息
 *
 * @param taskId  自定义的任务id
 * @param flow    绘图任务工作流
 * @param timeout 任务超时时间
 * @param unit    超时时间单位
 */
public record DrawingTaskInfo(String taskId, ComfyUIWorkFlow flow, long timeout, TimeUnit unit) {
}
