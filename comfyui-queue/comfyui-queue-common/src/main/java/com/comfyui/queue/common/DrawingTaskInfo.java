package com.comfyui.queue.common;

import com.comfyui.common.entity.ComfyWorkFlow;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.concurrent.TimeUnit;

/**
 * 绘图任务信息
 *
 * @author Sun_12138
 */
@Getter
public class DrawingTaskInfo {

    /**
     * 自定义的任务id
     */
    private final String taskId;

    /**
     * 绘图任务工作流
     */
    private final ComfyWorkFlow flow;

    /**
     * 任务超时时间
     */
    private final long timeout;

    /**
     * 超时时间单位
     */
    private final TimeUnit unit;

    /**
     * @param taskId  自定义的任务id
     * @param flow    绘图任务工作流
     * @param timeout 任务超时时间
     * @param unit    超时时间单位
     */
    @ConstructorProperties({"taskId", "flow", "timeout", "unit"})
    public DrawingTaskInfo(String taskId, ComfyWorkFlow flow, long timeout, TimeUnit unit) {
        this.taskId = taskId;
        this.flow = flow;
        this.timeout = timeout;
        this.unit = unit;
    }
}
