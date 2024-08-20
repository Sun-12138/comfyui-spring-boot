package com.comfyui.autoconfigure;

import com.comfyui.autoconfigure.properties.ComfyQueueProperties;
import com.comfyui.monitor.message.TaskProcessSender;
import com.comfyui.queue.common.DrawingTaskExecutor;
import com.comfyui.queue.common.IDrawingTaskSubmit;
import com.comfyui.queue.rabbitmq.DrawingTaskRabbitService;
import com.comfyui.queue.rabbitmq.config.RabbitMonitorQueueConfig;
import com.comfyui.queue.rabbitmq.config.RabbitQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sun_12138
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RabbitAutoConfiguration.class)
@ConditionalOnProperty(name = ComfyQueueProperties.PREFIX + ".type", havingValue = "rabbit")
public class ComfyRabbitAutoConfiguration {

    /**
     * @param amqpTemplate rabbitmq对象
     * @param drawingTaskExecutor 绘图任务执行者
     * @return 绘图任务提交对象
     */
    @Bean
    public IDrawingTaskSubmit drawingTaskRabbitService(AmqpTemplate amqpTemplate, DrawingTaskExecutor drawingTaskExecutor) {
        log.info("ComfyUITaskQueue: rabbitmq");
        return new DrawingTaskRabbitService(amqpTemplate, drawingTaskExecutor);
    }

    /**
     * 绘图任务队列
     *
     * @return 绘图任务队列
     */
    @Bean(RabbitQueueConfig.TASK_QUEUE)
    public Queue taskQueue() {
        return QueueBuilder.durable(RabbitQueueConfig.TASK_QUEUE)
                .build();
    }

    /**
     * 绘图任务交换机
     *
     * @return 绘图任务交换机
     */
    @Bean(RabbitQueueConfig.TASK_EXCHANGE)
    public DirectExchange taskExchange() {
        return new DirectExchange(RabbitQueueConfig.TASK_EXCHANGE);
    }

    /**
     * 绘图任务进度扇形交换机
     *
     * @return 绘图任务进度扇形交换机
     */
    @Bean(RabbitMonitorQueueConfig.TASK_MONITOR_EXCHANGE)
    public FanoutExchange monitorExchange() {
        return new FanoutExchange(RabbitMonitorQueueConfig.TASK_MONITOR_EXCHANGE);
    }

    /**
     * @param queue    绘图任务队列
     * @param exchange 绘图任务交换机
     * @return 绘图任务队列绑定
     */
    @Bean
    public Binding bindingDirect(@Qualifier(RabbitQueueConfig.TASK_QUEUE) Queue queue,
                                 @Qualifier(RabbitQueueConfig.TASK_EXCHANGE) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitQueueConfig.TASK_ROUTING_KEY);
    }
}
