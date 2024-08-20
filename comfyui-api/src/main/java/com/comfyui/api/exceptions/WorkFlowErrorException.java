package com.comfyui.api.exceptions;

/**
 * 工作流错误
 * @author Sun_12138
 */
public class WorkFlowErrorException extends RuntimeException {

    /**
     * @param message 错误信息
     */
    public WorkFlowErrorException(String message) {
        super(message);
    }
}
