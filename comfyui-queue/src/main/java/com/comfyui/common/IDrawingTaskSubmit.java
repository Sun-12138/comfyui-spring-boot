package com.comfyui.common;

/**
 * 绘图任务提交接口
 * @author Sun_12138
 */
public interface IDrawingTaskSubmit {

    /**
     * 提交任务
     *
     * @param taskInfo 任务信息
     * @return 是否成功
     */
    boolean submit(DrawingTaskInfo taskInfo);
}
