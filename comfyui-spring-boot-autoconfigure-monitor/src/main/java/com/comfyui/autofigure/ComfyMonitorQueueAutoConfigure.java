package com.comfyui.autofigure;

import com.comfyui.monitor.rabbitmq.MonitorQueueNameHolder;
import com.comfyui.queue.rabbitmq.config.RabbitMonitorQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sun_12138
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RabbitAutoConfiguration.class)
public class ComfyMonitorQueueAutoConfigure {

    /**
     * @return 生成随机生成队列名
     */
    @Bean
    public MonitorQueueNameHolder monitorQueueNameHolder() {
        return new MonitorQueueNameHolder();
    }

    /**
     * @param queueNameHolder 队列名生成器
     * @return 绘图任务进度监听队列
     */
    @Bean(RabbitMonitorQueueConfig.TASK_MONITOR_QUEUE)
    public Queue taskQueue(MonitorQueueNameHolder queueNameHolder) {
        return QueueBuilder.durable(queueNameHolder.queueName)
                .autoDelete()
                .build();
    }

    /**
     * @return 绘图任务进度扇形交换机
     */
    @Bean(RabbitMonitorQueueConfig.TASK_MONITOR_EXCHANGE)
    public FanoutExchange monitorExchange() {
        return new FanoutExchange(RabbitMonitorQueueConfig.TASK_MONITOR_EXCHANGE);
    }

    /**
     * @param queue    任务进度队列
     * @param exchange 扇形交换机
     * @return 绑定任务进度队列和扇形交换机
     */
    @Bean
    public Binding bindingFanout(@Qualifier(RabbitMonitorQueueConfig.TASK_MONITOR_QUEUE) Queue queue,
                                 @Qualifier(RabbitMonitorQueueConfig.TASK_MONITOR_EXCHANGE) FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

}
