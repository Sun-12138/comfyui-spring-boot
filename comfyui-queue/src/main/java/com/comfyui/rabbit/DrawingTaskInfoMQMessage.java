package com.comfyui.rabbit;

import com.comfyui.utils.JsonUtils;
import com.comfyui.common.DrawingTaskInfo;

/**
 * 绘图任务信息消息队列信息对象
 *
 * @param taskInfo 绘图任务信息
 */
public record DrawingTaskInfoMQMessage(DrawingTaskInfo taskInfo) {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
