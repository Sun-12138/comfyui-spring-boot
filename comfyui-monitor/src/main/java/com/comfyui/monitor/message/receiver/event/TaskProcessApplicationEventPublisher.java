package com.comfyui.monitor.message.receiver.event;

import com.comfyui.annotation.enums.TaskProcessType;
import com.comfyui.common.process.*;
import com.comfyui.monitor.message.receiver.ITaskProcessReceiver;
import com.comfyui.monitor.message.receiver.TaskProcessMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

/**
 * ComfyUI绘图进度Application事件发布者
 *
 * @author Sun_12138
 */
@RequiredArgsConstructor
public class TaskProcessApplicationEventPublisher implements ITaskProcessReceiver {

    private final ApplicationEventPublisher publisher;

    /**
     * 任务开始
     *
     * @param start 任务开始信息
     */
    @Override
    public void taskStart(ComfyTaskStart start) {
        publishEvent(TaskProcessType.START, start.getTaskId(), start);
    }

    /**
     * 当前执行的节点和节点执行进度
     *
     * @param progress 进度信息
     */
    @Override
    public void taskNodeProgress(ComfyTaskNodeProgress progress) {
        publishEvent(TaskProcessType.PROGRESS, progress.getTaskId(), progress);
    }

    /**
     * 任务进度预览效果图
     *
     * @param preview 预览图信息
     */
    @Override
    public void taskProgressPreview(ComfyTaskProgressPreview preview) {
        publishEvent(TaskProcessType.PREVIEW, preview.getTaskId(), preview);
    }

    /**
     * 任务输出的图片
     *
     * @param output 输出信息
     */
    @Override
    public void taskOutput(ComfyTaskOutput output) {
        publishEvent(TaskProcessType.OUTPUT, output.getTaskId(), output);
    }

    /**
     * 任务完成
     *
     * @param complete 任务完成信息
     */
    @Override
    public void taskComplete(ComfyTaskComplete complete) {
        publishEvent(TaskProcessType.COMPLETE, complete.getTaskId(), complete);
    }

    /**
     * 任务失败
     *
     * @param error 任务错误信息
     */
    @Override
    public void taskError(ComfyTaskError error) {
        publishEvent(TaskProcessType.ERROR, error.getTaskId(), error);
    }

    /**
     * 绘图队列任务个数更新
     *
     * @param taskNumber 队列任务信息
     */
    @Override
    public void taskNumberUpdate(ComfyTaskNumber taskNumber) {
        publishEvent(TaskProcessType.NUMBER_UPDATE, taskNumber);
    }

    /**
     * 当前系统负载
     *
     * @param performance 系统状态
     */
    @Override
    public void systemPerformance(ComfySystemPerformance performance) {
        publishEvent(TaskProcessType.SYSTEM_PERFORMANCE, performance);
    }

    /**
     * 发布事件
     *
     * @param eventType 任务类型
     * @param process   任务消息
     */
    public void publishEvent(TaskProcessType eventType, IComfyTaskProcess process) {
        publishEvent(eventType, null, process);
    }

    /**
     * 发布事件
     *
     * @param eventType 任务类型
     * @param taskId    任务id
     * @param process   任务消息
     */
    public void publishEvent(TaskProcessType eventType, String taskId, IComfyTaskProcess process) {
        publisher.publishEvent(new TaskProcessMessage<>(eventType, taskId, process, this));
    }
}
