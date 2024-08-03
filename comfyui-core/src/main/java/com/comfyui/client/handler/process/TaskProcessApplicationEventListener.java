package com.comfyui.client.handler.process;

import com.comfyui.annotation.ComfyUITaskProcessListener;
import com.comfyui.entity.IComfyUITaskProcess;
import com.comfyui.client.enums.TaskProcessType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ComfyUI绘图进度Application事件监听者
 */
@Component
public class TaskProcessApplicationEventListener implements BeanPostProcessor {

    /**
     * key为订阅的任务进度类型 value为目标方法列表
     */
    private final Map<TaskProcessType, List<InvocableHandlerMethod>> targetMap = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            ComfyUITaskProcessListener annotation = method.getAnnotation(ComfyUITaskProcessListener.class);
            //判断方法是否含有ComfyUITaskProcessListener注解
            if (annotation != null) {
                //确认该方法可以被访问
                method.setAccessible(true);
                TaskProcessType type = annotation.value();
                targetMap.computeIfAbsent(type, k -> new ArrayList<>())
                        .add(new InvocableHandlerMethod(bean, method));
            }
        }
        return bean;
    }

    /**
     * 调用绑定对应事件类型的方法
     */
    @EventListener
    public void handleEvent(TaskProcessInfo<IComfyUITaskProcess> event) {
        List<InvocableHandlerMethod> values = targetMap.getOrDefault(event.getEventType(), new ArrayList<>());
        values.forEach(value -> {
            Message<MessagePayload<IComfyUITaskProcess>> message = MessageBuilder
                    .withPayload(new MessagePayload<>(event.getTaskId(), event.getData()))
                    .build();
            try {
                //调用目标方法
                value.invoke(message, event.getTaskId(), event.getData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private record MessagePayload<T>(String virtualTaskId, T method){}
}
