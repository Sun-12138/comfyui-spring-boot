package com.comfyui.queue.rabbitmq.message;

import com.comfyui.annotation.enums.TaskProcessType;
import com.comfyui.common.utils.JsonUtils;
import com.comfyui.common.process.IComfyTaskProcess;
import lombok.Getter;

import java.beans.ConstructorProperties;

/**
 * 绘图任务进度消息
 *
 * @author Sun_12138
 */
@Getter
public class DrawingTaskProcessMessage {

    /**
     * 进度类型
     */
    private final TaskProcessType type;

    /**
     * 进度消息数据
     */
    private final IComfyTaskProcess info;

    /**
     * @param type 进度类型
     * @param info 进度消息数据
     */
    @ConstructorProperties({"type", "info"})
    public DrawingTaskProcessMessage(TaskProcessType type, IComfyTaskProcess info) {
        this.type = type;
        this.info = info;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
