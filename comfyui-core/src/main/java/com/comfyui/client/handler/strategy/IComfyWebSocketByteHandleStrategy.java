package com.comfyui.client.handler.strategy;

import com.comfyui.client.handler.TaskProcessSender;

/**
 * 处理ComfyUI ws的二进制消息
 * @author Sun_12138
 */
public interface IComfyWebSocketByteHandleStrategy {

    /**
     * 处理二进制消息
     *
     * @param bytes  二进制数组
     * @param caller 任务处理结果接收对象
     * @param ctx    上下文任务状态
     */
    void handleByte(byte[] bytes, TaskProcessSender caller, TaskHandlerStrategyContext ctx);
}
