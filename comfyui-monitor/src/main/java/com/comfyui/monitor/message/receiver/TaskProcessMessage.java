package com.comfyui.monitor.message.receiver;

import com.comfyui.annotation.enums.TaskProcessType;
import com.comfyui.common.process.IComfyTaskProcess;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 任务进度消息
 *
 * @author Sun_12138
 * @param <T> 消息类型
 */
@Getter
public class TaskProcessMessage<T extends IComfyTaskProcess> extends ApplicationEvent {

    /**
     * 任务进度事件类型
     */
    private final TaskProcessType eventType;

    /**
     * 任务id
     */
    private final String taskId;

    /**
     * 务进度内容
     */
    private final T data;

    /**
     * @param eventType 任务进度事件类型
     * @param taskId 任务id
     * @param data 任务进度内容
     * @param source 事件最初发生或与事件关联的对象（永不为null ）
     */
    public TaskProcessMessage(final TaskProcessType eventType, final String taskId, final T data, Object source) {
        super(source);
        this.eventType = eventType;
        this.taskId = taskId;
        this.data = data;
    }
}
