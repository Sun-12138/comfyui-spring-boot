package com.comfyui.queue;

import com.comfyui.common.DrawingTaskExecutor;
import com.comfyui.common.IDrawingTaskSubmit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ComfyUIQueueProperties.class)
public class ComfyUIQueueAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = ComfyUIQueueProperties.PREFIX + ".type", havingValue = "queue")
    public IDrawingTaskSubmit queueTaskSubmitStrategy(DrawingTaskExecutor taskExecutor) {
        log.info("ComfyUITaskQueue: queue");
        return new DrawingTaskQueueManager(taskExecutor);
    }
}
