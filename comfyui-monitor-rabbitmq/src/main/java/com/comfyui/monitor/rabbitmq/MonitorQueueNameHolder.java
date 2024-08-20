package com.comfyui.monitor.rabbitmq;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.comfyui.queue.rabbitmq.config.RabbitMonitorQueueConfig;

/**
 * 用于生成随机生成队列名
 *
 * @author Sun_12138
 */
public class MonitorQueueNameHolder {

    /**
     * 任务进度队列名
     */
    public final String queueName;

    /**
     * 默认无参构造
     */
    public MonitorQueueNameHolder() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        this.queueName = RabbitMonitorQueueConfig.TASK_MONITOR_QUEUE + "&" + snowflake.nextIdStr();
    }
}
