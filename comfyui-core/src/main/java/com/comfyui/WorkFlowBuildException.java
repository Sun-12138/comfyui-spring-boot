package com.comfyui;

/**
 * 序列化创建工作流对象时错误
 */
public class WorkFlowBuildException extends RuntimeException {

    /**
     * @param message 错误信息
     */
    public WorkFlowBuildException(String message) {
        super(message);
    }
}
