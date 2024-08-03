package com.comfyui.queue;

import com.comfyui.common.DrawingTaskExecutor;
import com.comfyui.common.IDrawingTaskSubmit;
import com.comfyui.rabbit.DrawingTaskRabbitMQService;
import com.comfyui.rabbit.RabbitQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AutoConfigureAfter(RabbitAutoConfiguration.class)
@ConditionalOnProperty(name = ComfyUIQueueProperties.PREFIX + ".type", havingValue = "rabbit")
public class ComfyUIRabbitAutoConfiguration {

    @Bean
    public IDrawingTaskSubmit rabbitTaskSubmitStrategy(AmqpTemplate amqpTemplate, DrawingTaskExecutor taskExecutor) {
        log.info("ComfyUITaskQueue: rabbit");
        return new DrawingTaskRabbitMQService(amqpTemplate, taskExecutor);
    }

    /**
     * 绘图任务队列
     */
    @Bean(RabbitQueueConfig.TASK_QUEUE)
    public Queue taskQueue() {
        return QueueBuilder.durable(RabbitQueueConfig.TASK_QUEUE)
                .build();
    }


    /**
     * 绘图任务交换机
     */
    @Bean(RabbitQueueConfig.TASK_EXCHANGE)
    public DirectExchange taskExchange() {
        return new DirectExchange(RabbitQueueConfig.TASK_EXCHANGE);
    }

    /**
     * 绘图任务进度扇形交换机
     */
    @Bean(RabbitQueueConfig.TASK_MONITOR_EXCHANGE)
    public FanoutExchange monitorExchange() {
        return new FanoutExchange(RabbitQueueConfig.TASK_MONITOR_EXCHANGE);
    }


    /**
     * 绘图任务队列绑定
     */
    @Bean
    public Binding bindingDirect(@Qualifier(RabbitQueueConfig.TASK_QUEUE) Queue queue,
                                 @Qualifier(RabbitQueueConfig.TASK_EXCHANGE) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitQueueConfig.TASK_ROUTING_KEY);
    }
}
