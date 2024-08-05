package com.comfyui.client.handler.process;

import com.comfyui.annotation.TaskProcessListener;
import com.comfyui.entity.ComfyTaskStart;
import com.comfyui.entity.IComfyTaskProcess;
import com.comfyui.client.enums.TaskProcessType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.event.EventListener;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ComfyUI绘图进度Application事件监听者
 *
 * @author Sun_12138
 */
@Component
public class TaskProcessApplicationEventListener implements BeanPostProcessor {

    /**
     * key为订阅的任务进度类型 value为目标方法列表
     */
    private final Map<TaskProcessType, List<Value>> targetMap = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, @Nullable String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            TaskProcessListener annotation = method.getAnnotation(TaskProcessListener.class);
            //判断方法是否含有ComfyUITaskProcessListener注解
            if (annotation != null) {
                // 检查方法是否可访问
                if (!Modifier.isPublic(method.getModifiers())) {
                    // 设置为可访问
                    try {
                        method.setAccessible(true);
                    } catch (Exception e) {
                        //方法无法访问
                        throw new RuntimeException("Error method '" + method.getName() + "', defined in '" + bean.getClass() + "', This method cannot be accessed");
                    }
                }
                TaskProcessType type = annotation.value();
                //检查方法类型是否正确
                if (!checkMethodParameter(type, method.getParameterTypes())) {
                    throw new RuntimeException("Error method '" + method.getName() + "', defined in '" + bean.getClass() + "' The accepted parameters can only be '" + type.getEntityClass().getTypeName() + "'");
                }
                ;
                targetMap.computeIfAbsent(type, k -> new ArrayList<>())
                        .add(new Value(bean, method));
            }
        }
        return bean;
    }

    /**
     * 检查方法参数是否正确
     *
     * @param processType    进度类型
     * @param parameterTypes 方法参数类型
     */
    private boolean checkMethodParameter(TaskProcessType processType, Class<?>[] parameterTypes) {
        if (parameterTypes.length != 1) {
            return false;
        }
        Class<?> superType = processType.getEntityClass();
        return superType == parameterTypes[0];
    }

    /**
     * 调用绑定对应事件类型的方法
     */
    @EventListener
    public void handleEvent(TaskProcessInfo<IComfyTaskProcess> event) {
        List<Value> values = targetMap.getOrDefault(event.getEventType(), new ArrayList<>());
        values.forEach(value -> {
            try {
                //调用目标方法
                Method targetMethod = value.method();
                targetMethod.invoke(value.bean(), event.getData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * @param bean   方法所属bean对象
     * @param method 方法对象
     */
    private record Value(Object bean, Method method) {
    }
}
