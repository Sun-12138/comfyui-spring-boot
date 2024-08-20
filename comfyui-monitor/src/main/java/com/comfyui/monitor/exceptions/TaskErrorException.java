package com.comfyui.monitor.exceptions;

/**
 * ComfyUI绘图任务发生错误
 * @author Sun_12138
 */
public class TaskErrorException extends RuntimeException {

    /**
     * @param taskId 发生错误的任务id
     * @param message 错误信息
     */
    public TaskErrorException(String taskId, String message) {
        super(message + ", 错误的任务id:" + taskId);
    }
}
