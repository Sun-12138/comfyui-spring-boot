package com.comfyui;

/**
 * ComfyUI绘图任务超时
 */
public class TaskTimeoutException extends RuntimeException {

    /**
     * @param taskId 超时的任务id
     */
    public TaskTimeoutException(String taskId) {
        super("绘图任务超时, 超时的任务id:" + taskId);
    }

    /**
     * @param taskId 超时的任务id
     * @param message 超时信息
     */
    public TaskTimeoutException(String taskId, String message) {
        super(message + ", 超时的任务id:" + taskId);
    }
}
