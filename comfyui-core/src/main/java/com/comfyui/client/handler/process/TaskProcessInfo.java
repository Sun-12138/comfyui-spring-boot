package com.comfyui.client.handler.process;

import com.comfyui.client.enums.TaskProcessType;
import com.comfyui.entity.IComfyUITaskProcess;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 任务进度消息
 *
 * @param <T> 消息类型
 */
@Getter
public class TaskProcessInfo<T extends IComfyUITaskProcess> extends ApplicationEvent {

    /**
     * 事件类型
     */
    private final TaskProcessType eventType;

    /**
     * 任务id
     */
    private final String taskId;

    /**
     * 消息内容
     */
    private final T data;

    public TaskProcessInfo(final TaskProcessType eventType, final String taskId, final T data, Object source) {
        super(source);
        this.eventType = eventType;
        this.taskId = taskId;
        this.data = data;
    }
}
