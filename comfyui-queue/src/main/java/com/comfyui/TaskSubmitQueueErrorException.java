package com.comfyui;

/**
 * 绘图任务提交任务队列失败
 */
public class TaskSubmitQueueErrorException extends Exception {

    /**
     * @param message 错误信息
     */
    public TaskSubmitQueueErrorException(String message) {
        super(message);
    }
}
