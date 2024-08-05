package com.comfyui.rabbit;

import com.comfyui.client.enums.TaskProcessType;
import com.comfyui.entity.IComfyTaskProcess;
import com.comfyui.utils.JsonUtils;

/**
 * @author Sun_12138
 * @param type 进度类型
 * @param info 进度消息数据
 */
public record DrawingTaskProcessMessage(TaskProcessType type, IComfyTaskProcess info) {

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
