package com.comfyui.rabbit;

import com.comfyui.client.enums.TaskProcessType;
import com.comfyui.entity.IComfyUITaskProcess;
import com.comfyui.utils.JsonUtils;

/**
 * @param type 进度类型
 * @param info 进度消息数据
 */
public record DrawingTaskProcessMQMessage(TaskProcessType type, IComfyUITaskProcess info) {

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
