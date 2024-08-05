package com.comfyui.client.handler.process;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * ComfyUI绘图进度Application事件发布者
 * @author Sun_12138
 */
@Component
@RequiredArgsConstructor
public class TaskProcessApplicationEventPublisher {

    private final ApplicationEventPublisher publisher;

    /**
     * 发布事件
     *
     * @param subject 发布内容
     */
    public void publishEvent(TaskProcessInfo<?> subject) {
        publisher.publishEvent(subject);
    }
}
