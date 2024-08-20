package com.comfyui.autofigure;

import com.comfyui.monitor.message.TaskProcessSender;
import com.comfyui.monitor.message.receiver.ITaskProcessReceiver;
import com.comfyui.monitor.message.receiver.event.TaskProcessApplicationEventListener;
import com.comfyui.monitor.message.receiver.event.TaskProcessApplicationEventPublisher;
import com.comfyui.monitor.message.receiver.subject.TaskProcessSubjectPublisher;
import com.comfyui.monitor.rabbitmq.MonitorQueueNameHolder;
import com.comfyui.monitor.rabbitmq.TaskProcessRabbitMonitor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * @author Sun_12138
 */
@Configuration
@RequiredArgsConstructor
public class ComfyRabbitMonitorAutoConfigure {

    private final ApplicationContext appCtx;

    /**
     * @return 任务进度发送者
     */
    @Bean
    public TaskProcessSender taskProcessSender() {
        return new TaskProcessSender();
    }

    /**
     * @param sender 任务进度发送者
     * @return 任务进度RabbitMQ监控器
     */
    @Bean
    public TaskProcessRabbitMonitor taskProcessRabbitMonitor(TaskProcessSender sender) {
        return new TaskProcessRabbitMonitor(sender);
    }

    /**
     * @param eventPublisher Spring事件发布者
     * @return ComfyUI绘图进度Application事件发布者
     */
    @Bean
    public TaskProcessApplicationEventPublisher taskProcessApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new TaskProcessApplicationEventPublisher(eventPublisher);
    }

    /**
     * @return ComfyUI任务进度主题发布者
     */
    @Bean
    public TaskProcessSubjectPublisher taskProcessSubjectPublisher() {
        return new TaskProcessSubjectPublisher();
    }

    /**
     * @return ComfyUI绘图进度Application事件监听器
     */
    @Bean
    public TaskProcessApplicationEventListener taskProcessApplicationEventListener() {
        return new TaskProcessApplicationEventListener();
    }

    /**
     * Spring Application启动完成时执行
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        TaskProcessSender processSender = appCtx.getBean(TaskProcessSender.class);
        //获取实现了ITaskProcessReceiver接口的bean 并将bean添加到TaskProcessSender对象中
        appCtx.getBeansOfType(ITaskProcessReceiver.class).values().forEach(processSender::addReceiver);
    }

}
