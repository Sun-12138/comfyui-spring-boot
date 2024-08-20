package com.comfyui.queue.rabbitmq.config;

/**
 * @author Sun_12138
 */
public class RabbitQueueConfig {
    /**
     * 绘图任务队列
     */
    public static final String TASK_QUEUE = "task.queue";

    /**
     * 绘图任务交换机
     */
    public static final String TASK_EXCHANGE = "task.exchange";

    /**
     * 绘图任务路由key
     */
    public static final String TASK_ROUTING_KEY = "task.key";
}
