package com.comfyui.common.process;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 用于标识ComfyUI WebSocket消息对象
 * @author Sun_12138
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public interface IComfyTaskProcess {
}
