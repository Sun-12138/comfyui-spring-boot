package com.comfyui.autoconfigure;

import com.comfyui.api.ComfyApiClient;
import com.comfyui.autoconfigure.properties.ComfyQueueProperties;
import com.comfyui.monitor.ComfyWebSocketClient;
import com.comfyui.monitor.message.TaskProcessSender;
import com.comfyui.monitor.message.receiver.subject.TaskProcessSubjectPublisher;
import com.comfyui.queue.common.DrawingTaskExecutor;
import com.comfyui.queue.common.IDrawingTaskSubmit;
import com.comfyui.queue.thread.DrawingTaskQueueService;
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

    /**
     * @param publisher 任务进度主题发布者
     * @param taskProcessSender 任务进度发送者
     * @param comfyClient ComfyUI服务端
     * @param comfyWebSocketClient ComfyUI WebSocket任务进度监听
     * @return 绘图任务执行者
     */
    @Bean
    public DrawingTaskExecutor drawingTaskExecutor(
            TaskProcessSubjectPublisher publisher,
            TaskProcessSender taskProcessSender,
            ComfyApiClient comfyClient,
            ComfyWebSocketClient comfyWebSocketClient) {
        return new DrawingTaskExecutor(publisher, taskProcessSender, comfyClient, comfyWebSocketClient);
    }

    /**
     * @param drawingTaskExecutor 绘图任务执行者
     * @return 绘图任务提交对象
     */
    @Bean
    @ConditionalOnProperty(name = ComfyQueueProperties.PREFIX + ".type", havingValue = "thread")
    public IDrawingTaskSubmit queueTaskSubmitStrategy(DrawingTaskExecutor drawingTaskExecutor) {
        log.info("ComfyUITaskQueue: thread");
        return new DrawingTaskQueueService(drawingTaskExecutor);
    }
}
