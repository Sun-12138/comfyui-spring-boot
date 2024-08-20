package com.comfyui.monitor.handler.strategy;

import com.comfyui.monitor.handler.TaskProcessContext;
import com.comfyui.monitor.message.TaskProcessSender;

/**
 * 处理ComfyUI ws的二进制消息
 *
 * @author Sun_12138
 */
public interface IComfyWebSocketByteHandleStrategy {

    /**
     * 处理二进制消息
     *
     * @param bytes         二进制数组
     * @param processSender 消息进度发送者
     * @param ctx           上下文任务状态
     */
    void handleByte(byte[] bytes, TaskProcessSender processSender, TaskProcessContext ctx);
}
