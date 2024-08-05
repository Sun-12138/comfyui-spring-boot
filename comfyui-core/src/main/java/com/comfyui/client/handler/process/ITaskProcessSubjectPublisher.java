package com.comfyui.client.handler.process;

import com.comfyui.client.enums.TaskProcessType;
import com.comfyui.entity.IComfyTaskProcess;

import java.util.*;

/**
 * 绘图任务进度发布者抽象类
 * @author Sun_12138
 */
public interface ITaskProcessSubjectPublisher {

    /**
     * key为类型
     */
    Map<Key, List<ITaskProcessSubjectSubscriber>> SUB_MAP = new HashMap<>();

    /**
     * 订阅事件 用于绑定
     *
     * @param eventType  事件类型
     * @param subscriber 订阅者
     */
    default void subscribe(TaskProcessType eventType, ITaskProcessSubjectSubscriber subscriber) {
        if (eventType != TaskProcessType.SYSTEM_PERFORMANCE && eventType != TaskProcessType.NUMBER_UPDATE) {
            throw new IllegalArgumentException("eventType must be system_performance or number_update");
        }
        subscribe(null, eventType, subscriber);
    }

    /**
     * 订阅事件
     *
     * @param taskId     任务id
     * @param eventType  事件类型
     * @param subscriber 订阅者
     */
    default void subscribe(String taskId, TaskProcessType eventType, ITaskProcessSubjectSubscriber subscriber) {
        SUB_MAP.computeIfAbsent(new Key(taskId, eventType), k -> new ArrayList<>()).add(subscriber);
    }

    /**
     * 发布事件
     *
     * @param eventType 事件类型
     * @param event     事件数据
     * @param <E>       事件数据类型
     */
    default <E extends IComfyTaskProcess> void publish(TaskProcessType eventType, TaskProcessInfo<E> event) {
        List<ITaskProcessSubjectSubscriber> subList = SUB_MAP.getOrDefault(new Key(event.getTaskId(), eventType), new ArrayList<>());
        for (ITaskProcessSubjectSubscriber sub : subList) {
            sub.notify(event);
        }
    }

    /**
     * 退订事件
     *
     * @param eventType  任务类型
     * @param subscriber 订阅者
     */
    default void unSubscribe(TaskProcessType eventType, ITaskProcessSubjectSubscriber subscriber) {
        if (eventType != TaskProcessType.SYSTEM_PERFORMANCE && eventType != TaskProcessType.NUMBER_UPDATE) {
            throw new IllegalArgumentException("eventType must be system_performance or number_update");
        }
        unSubscribe(null, eventType, subscriber);
    }

    /**
     * 退订事件
     *
     * @param taskId     任务id
     * @param eventType  任务类型
     * @param subscriber 订阅者
     */
    default void unSubscribe(String taskId, TaskProcessType eventType, ITaskProcessSubjectSubscriber subscriber) {
        if (eventType == TaskProcessType.SYSTEM_PERFORMANCE || eventType == TaskProcessType.NUMBER_UPDATE) {
            throw new IllegalArgumentException("eventType cannot be system_performance or number_update");
        }
        List<ITaskProcessSubjectSubscriber> subList = SUB_MAP.get(new Key(taskId, eventType));
        if (subList != null) {
            subList.remove(subscriber);
        }
    }

    /**
     * @param taskId    任务id
     * @param eventType 事件类型
     */
    record Key(String taskId, TaskProcessType eventType) {
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Key key = (Key) o;
            return Objects.equals(taskId, key.taskId) && Objects.equals(eventType, key.eventType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(taskId, eventType);
        }
    }
}
