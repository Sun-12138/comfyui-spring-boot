package com.comfyui.client.enums;

/**
 * 绘画任务进度类型
 */
public enum TaskProcessType {
    /**
     * 任务开始
     */
    START,
    /**
     * 进度更新
     */
    PROGRESS,
    /**
     * 预览图
     */
    PREVIEW,
    /**
     * 任务输出
     */
    OUTPUT,
    /**
     * 任务完成
     */
    COMPLETE,
    /**
     * 任务失败
     */
    ERROR,
    /**
     * 队列数量更新
     */
    NUMBER_UPDATE,
    /**
     * 系统状态更新
     */
    SYSTEM_PERFORMANCE
}
