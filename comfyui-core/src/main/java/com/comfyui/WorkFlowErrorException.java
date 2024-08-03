package com.comfyui;

/**
 * 工作流错误
 */
public class WorkFlowErrorException extends RuntimeException {

    public WorkFlowErrorException() {
    }

    /**
     * @param message 错误信息
     */
    public WorkFlowErrorException(String message) {
        super(message);
    }
}
