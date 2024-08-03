package com.comfyui.client.handler.process;

import com.comfyui.client.enums.TaskProcessType;
import com.comfyui.client.handler.ITaskProcessReceiver;
import com.comfyui.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * ComfyUI任务进度发布者 发布SpringApplication Event和发布订阅模式
 */
@Component
@RequiredArgsConstructor
public final class TaskProcessPublisher implements ITaskProcessReceiver, ITaskProcessSubjectPublisher {

    /**
     * Spring Application Event发布者
     */
    private final TaskProcessApplicationEventPublisher eventPublisher;

    /**
     * 任务开始
     *
     * @param start 任务开始信息
     */
    @Override
    public void taskStart(ComfyUITaskStart start) {
        publishApplicationEventAndSubject(TaskProcessType.START, start.taskId(), start);
    }

    /**
     * 当前执行的节点和节点执行进度
     *
     * @param progress 进度信息
     */
    @Override
    public void taskNodeProgress(ComfyUITaskNodeProgress progress) {
        publishApplicationEventAndSubject(TaskProcessType.PROGRESS, progress.taskId(), progress);
    }

    /**
     * 任务进度预览效果图
     *
     * @param preview 预览图信息
     */
    @Override
    public void taskProgressPreview(ComfyUITaskProgressPreview preview) {
        publishApplicationEventAndSubject(TaskProcessType.PREVIEW, preview.taskId(), preview);
    }

    /**
     * 任务输出的图片
     *
     * @param output 输出信息
     */
    @Override
    public void taskOutput(ComfyUITaskOutput output) {
        publishApplicationEventAndSubject(TaskProcessType.OUTPUT, output.taskId(), output);
    }

    /**
     * 任务完成
     *
     * @param complete 任务完成信息
     */
    @Override
    public void taskComplete(ComfyUITaskComplete complete) {
        publishApplicationEventAndSubject(TaskProcessType.COMPLETE, complete.taskId(), complete);
    }

    /**
     * 任务失败
     *
     * @param error 任务错误信息
     */
    @Override
    public void taskError(ComfyUITaskError error) {
        publishApplicationEventAndSubject(TaskProcessType.ERROR, error.taskId(), error);
    }

    /**
     * 绘图队列任务个数更新
     *
     * @param taskNumber 队列任务信息
     */
    @Override
    public void taskNumberUpdate(ComfyUITaskNumber taskNumber) {
        publishApplicationEventAndSubject(TaskProcessType.NUMBER_UPDATE, taskNumber);
    }

    /**
     * 当前系统负载
     *
     * @param performance 系统状态
     */
    @Override
    public void systemPerformance(ComfyUISystemPerformance performance) {
        publishApplicationEventAndSubject(TaskProcessType.SYSTEM_PERFORMANCE, performance);
    }

    /**
     * 发送Application事件和subject主题
     *
     * @param eventType 事件类型
     * @param process   事件数据
     */
    private void publishApplicationEventAndSubject(TaskProcessType eventType, IComfyUITaskProcess process) {
        TaskProcessInfo<?> subject = new TaskProcessInfo<>(eventType, null, process, this);
        publish(eventType, subject);
        eventPublisher.publishEvent(subject);
    }

    /**
     * 发送Application事件和subject主题
     *
     * @param eventType 事件类型
     * @param taskId    任务id
     * @param process   事件数据
     */
    private void publishApplicationEventAndSubject(TaskProcessType eventType, String taskId, IComfyUITaskProcess process) {
        TaskProcessInfo<?> subject = new TaskProcessInfo<>(eventType, taskId, process, this);
        publish(eventType, subject);
        eventPublisher.publishEvent(subject);
    }
}
