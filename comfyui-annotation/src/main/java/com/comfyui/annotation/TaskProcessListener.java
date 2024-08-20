package com.comfyui.annotation;

import com.comfyui.annotation.enums.TaskProcessType;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 监听ComfyUI绘图任务进度
 *
 * @author Sun_12138
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MessageMapping
public @interface TaskProcessListener {
    /**
     * @return 监听的进度类型
     */
    TaskProcessType value();
}
