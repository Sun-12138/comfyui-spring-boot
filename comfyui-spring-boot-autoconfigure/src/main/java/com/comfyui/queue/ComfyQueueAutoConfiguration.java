package com.comfyui.queue;

import com.comfyui.common.DrawingTaskExecutor;
import com.comfyui.common.IDrawingTaskSubmit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sun_12138
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ComfyQueueProperties.class)
public class ComfyQueueAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = ComfyQueueProperties.PREFIX + ".type", havingValue = "queue")
    public IDrawingTaskSubmit queueTaskSubmitStrategy(DrawingTaskExecutor taskExecutor) {
        log.info("ComfyUITaskQueue: queue");
        return new DrawingTaskQueueService(taskExecutor);
    }
}
