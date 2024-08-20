package com.comfyui.monitor.message.receiver.subject;

import com.comfyui.annotation.enums.TaskProcessType;
import com.comfyui.common.process.IComfyTaskProcess;
import com.comfyui.monitor.message.receiver.TaskProcessMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

/**
 * 绘图任务进度发布者抽象类
 *
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
        SUB_MAP.computeIfAbsent(new Key(taskId, eventType), k -> Collections.synchronizedList(new ArrayList<>())).add(subscriber);
    }

    /**
     * 发布事件
     *
     * @param eventType 事件类型
     * @param event     事件数据
     * @param <E>       事件数据类型
     */
    default <E extends IComfyTaskProcess> void publish(TaskProcessType eventType, TaskProcessMessage<E> event) {
        //通知绑定当前任务id和对应事件类型的订阅者
        List<ITaskProcessSubjectSubscriber> subList = SUB_MAP.getOrDefault(new Key(event.getTaskId(), eventType), new ArrayList<>());
        synchronized (subList) {
            for (ITaskProcessSubjectSubscriber sub : subList) {
                sub.notify(event);
            }
        }
        //通知绑定当前事件类型的订阅者
        List<ITaskProcessSubjectSubscriber> subList2 = SUB_MAP.getOrDefault(new Key(null, eventType), new ArrayList<>());
        synchronized (subList2) {
            for (ITaskProcessSubjectSubscriber sub : subList2) {
                sub.notify(event);
            }
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
     * Map组合键
     */
    @Getter
    @EqualsAndHashCode
    class Key {
        /**
         * 任务id
         */
        private final String taskId;

        /**
         * 事件类型
         */
        private final TaskProcessType eventType;

        /**
         * @param taskId    任务id
         * @param eventType 事件类型
         */
        public Key(String taskId, TaskProcessType eventType) {
            this.taskId = taskId;
            this.eventType = eventType;
        }
    }
}
