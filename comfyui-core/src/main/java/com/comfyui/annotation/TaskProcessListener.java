package com.comfyui.annotation;

import com.comfyui.client.enums.TaskProcessType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 监听ComfyUI绘图任务进度
 * @author Sun_12138
 */
@Target({ElementType.METHOD, ElementType.TYPE}) // 仅用于方法
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskProcessListener {
    /**
     * 监听的进度类型
     */
    TaskProcessType value();
}
