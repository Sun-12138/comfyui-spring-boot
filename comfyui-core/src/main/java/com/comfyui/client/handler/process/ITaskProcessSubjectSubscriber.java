package com.comfyui.client.handler.process;

import com.comfyui.entity.IComfyUITaskProcess;

/**
 * 绘画任务进度订阅者接口
 */
public interface ITaskProcessSubjectSubscriber {
    <T extends IComfyUITaskProcess> void notify(TaskProcessInfo<T> event);
}
