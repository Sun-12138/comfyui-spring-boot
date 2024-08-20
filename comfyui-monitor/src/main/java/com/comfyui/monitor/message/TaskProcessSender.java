package com.comfyui.monitor.message;

import com.comfyui.common.process.*;
import com.comfyui.monitor.message.receiver.ITaskProcessReceiver;

import java.util.HashSet;
import java.util.Set;

/**
 * 任务进度发送者 将任务进度发送到ITaskProcessReceiver接口
 *
 * @author Sun_12138
 */
public class TaskProcessSender implements ITaskProcessReceiver {

    /**
     * 全部IComfyUITaskProcessHandler的bean
     */
    private final Set<ITaskProcessReceiver> receivers;

    /**
     * 默认无参构造
     */
    public TaskProcessSender() {
        this.receivers = new HashSet<>();
    }

    /**
     * @param receivers 任务进度接收者对象列表
     */
    public TaskProcessSender(Set<ITaskProcessReceiver> receivers) {
        this.receivers = new HashSet<>();
        this.receivers.addAll(receivers);
    }

    /**
     * 添加新的接收者
     *
     * @param receiver 新的接收者
     */
    public void addReceiver(ITaskProcessReceiver receiver) {
        if (receiver != this) {
            receivers.add(receiver);
        }
    }

    /**
     * 任务开始
     *
     * @param start 任务开始信息
     */
    @Override
    public void taskStart(ComfyTaskStart start) {
        receivers.forEach(handler -> handler.taskStart(start));
    }

    /**
     * 当前执行的节点和节点执行进度
     *
     * @param progress 进度信息
     */
    @Override
    public void taskNodeProgress(ComfyTaskNodeProgress progress) {
        receivers.forEach(handler -> handler.taskNodeProgress(progress));
    }

    /**
     * 任务进度预览效果图
     *
     * @param preview 预览图信息
     */
    @Override
    public void taskProgressPreview(ComfyTaskProgressPreview preview) {
        receivers.forEach(handler -> handler.taskProgressPreview(preview));
    }

    /**
     * 任务输出的图片
     *
     * @param output 输出信息
     */
    @Override
    public void taskOutput(ComfyTaskOutput output) {
        receivers.forEach(handler -> handler.taskOutput(output));
    }

    /**
     * 任务完成
     *
     * @param complete 任务完成信息
     */
    @Override
    public void taskComplete(ComfyTaskComplete complete) {
        receivers.forEach(handler -> handler.taskComplete(complete));
    }

    /**
     * 任务失败
     *
     * @param error 任务错误信息
     */
    @Override
    public void taskError(ComfyTaskError error) {
        receivers.forEach(handler -> handler.taskError(error));
    }

    /**
     * 绘图队列任务个数更新
     *
     * @param taskNumber 队列任务信息
     */
    @Override
    public void taskNumberUpdate(ComfyTaskNumber taskNumber) {
        receivers.forEach(handler -> handler.taskNumberUpdate(taskNumber));
    }

    /**
     * 当前系统负载
     *
     * @param performance 系统状态
     */
    @Override
    public void systemPerformance(ComfySystemPerformance performance) {
        receivers.forEach(handler -> handler.systemPerformance(performance));
    }
}
