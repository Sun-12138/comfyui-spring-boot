package com.comfyui.queue.rabbitmq.message;

import com.comfyui.common.utils.JsonUtils;
import com.comfyui.queue.common.DrawingTaskInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 绘图任务信息消息队列信息对象
 *
 * @author Sun_12138
 */
@Getter
public class DrawingTaskInfoMessage {

    /**
     * 绘图任务信息
     */
    private final DrawingTaskInfo taskInfo;

    /**
     * @param taskInfo 绘图任务信息
     */
    @JsonCreator
    public DrawingTaskInfoMessage(@JsonProperty("taskInfo") DrawingTaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
