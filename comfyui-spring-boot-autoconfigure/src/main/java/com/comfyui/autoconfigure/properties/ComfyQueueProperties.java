package com.comfyui.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ComfyUI消息队列参数配置
 * @author Sun_12138
 */
@Data
@ConfigurationProperties(prefix = ComfyQueueProperties.PREFIX)
public class ComfyQueueProperties {

    /**
     * 配置前缀
     */
    public static final String PREFIX = "spring.comfyui.queue";

    /**
     * 队列类型
     */
    private QueueType type = QueueType.thread;

    enum QueueType {
        /**
         * 普通阻塞队列
         */
        thread,
        /**
         * RabbitMQ 请设置spring.rabbitmq.listener.simple.acknowledge = manual
         */
        rabbit,
    }
}
