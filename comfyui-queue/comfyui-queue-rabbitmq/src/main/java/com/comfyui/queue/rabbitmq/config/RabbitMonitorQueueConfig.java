package com.comfyui.queue.rabbitmq.config;

/**
 * @author Sun_12138
 */
public class RabbitMonitorQueueConfig {

    /**
     * 绘图任务进度监控交换机 扇形交换机
     */
    public static final String TASK_MONITOR_EXCHANGE = "task.monitor.exchange";


    /**
     * 绘图任务进度队列名前缀
     */
    public static final String TASK_MONITOR_QUEUE = "task.monitor.queue";

}
