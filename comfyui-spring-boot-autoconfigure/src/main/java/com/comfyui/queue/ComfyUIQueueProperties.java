package com.comfyui.queue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ComfyUI消息队列参数配置
 */
@Data
@ConfigurationProperties(prefix = ComfyUIQueueProperties.PREFIX)
public class ComfyUIQueueProperties {
    public static final String PREFIX = "spring.comfyui.queue";

    /**
     * 队列类型
     */
    private QueueType type = QueueType.queue;

    enum QueueType {
        /**
         * 普通队列
         */
        queue,
        /**
         * RabbitMQ 请设置spring.rabbitmq.listener.simple.acknowledge = manual
         */
        rabbit;
    }
}
