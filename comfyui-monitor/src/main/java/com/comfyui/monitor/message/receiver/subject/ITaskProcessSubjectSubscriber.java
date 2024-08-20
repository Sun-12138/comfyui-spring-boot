package com.comfyui.monitor.message.receiver.subject;

import com.comfyui.common.process.IComfyTaskProcess;
import com.comfyui.monitor.message.receiver.TaskProcessMessage;

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
    <T extends IComfyTaskProcess> void notify(TaskProcessMessage<T> event);
}
