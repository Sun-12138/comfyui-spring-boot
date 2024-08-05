package com.comfyui.client.handler.process;

import com.comfyui.entity.IComfyTaskProcess;

/**
 * 绘画任务进度订阅者接口
 * @author Sun_12138
 */
public interface ITaskProcessSubjectSubscriber {

    /**
     * 通知消息
     *
     * @param event 事件信息
     * @param <T> 任务进度类型
     */
    <T extends IComfyTaskProcess> void notify(TaskProcessInfo<T> event);
}
