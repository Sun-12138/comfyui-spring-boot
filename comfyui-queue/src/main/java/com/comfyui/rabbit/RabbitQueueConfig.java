package com.comfyui.rabbit;

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
     * 绘图任务进度监控交换机 扇形交换机
     */
    public static final String TASK_MONITOR_EXCHANGE = "task.monitor.exchange";

    /**
     * 绘图任务路由key
     */
    public static final String TASK_ROUTING_KEY = "task.key";
}
