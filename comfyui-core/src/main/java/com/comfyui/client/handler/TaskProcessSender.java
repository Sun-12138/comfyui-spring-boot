package com.comfyui.client.handler;

import com.comfyui.entity.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 任务进度发送者 将任务进度发送到ITaskProcessReceiver接口
 */
@Component
public class TaskProcessSender implements ITaskProcessReceiver {

    private final ApplicationContext applicationContext;

    /**
     * 全部IComfyUITaskProcessHandler的bean
     */
    private final Set<ITaskProcessReceiver> handlers = new HashSet<>();

    public TaskProcessSender(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.init();
    }

    /**
     * 初始化所有任务进度处理者
     */
    public void init() {
        handlers.addAll(applicationContext.getBeansOfType(ITaskProcessReceiver.class).values());
        //以防获取到当前对象 造成递归发送
        handlers.remove(this);
    }

    /**
     * 任务开始
     *
     * @param start 任务开始信息
     */
    @Override
    public void taskStart(ComfyUITaskStart start) {
        handlers.forEach(handler -> handler.taskStart(start));
    }

    /**
     * 当前执行的节点和节点执行进度
     *
     * @param progress 进度信息
     */
    @Override
    public void taskNodeProgress(ComfyUITaskNodeProgress progress) {
        handlers.forEach(handler -> handler.taskNodeProgress(progress));
    }

    /**
     * 任务进度预览效果图
     *
     * @param preview 预览图信息
     */
    @Override
    public void taskProgressPreview(ComfyUITaskProgressPreview preview) {
        handlers.forEach(handler -> handler.taskProgressPreview(preview));
    }

    /**
     * 任务输出的图片
     *
     * @param output 输出信息
     */
    @Override
    public void taskOutput(ComfyUITaskOutput output) {
        handlers.forEach(handler -> handler.taskOutput(output));
    }

    /**
     * 任务完成
     *
     * @param complete 任务完成信息
     */
    @Override
    public void taskComplete(ComfyUITaskComplete complete) {
        handlers.forEach(handler -> handler.taskComplete(complete));
    }

    /**
     * 任务失败
     *
     * @param error 任务错误信息
     */
    @Override
    public void taskError(ComfyUITaskError error) {
        handlers.forEach(handler -> handler.taskError(error));
    }

    /**
     * 绘图队列任务个数更新
     *
     * @param taskNumber 队列任务信息
     */
    @Override
    public void taskNumberUpdate(ComfyUITaskNumber taskNumber) {
        handlers.forEach(handler -> handler.taskNumberUpdate(taskNumber));
    }

    /**
     * 当前系统负载
     *
     * @param performance 系统状态
     */
    @Override
    public void systemPerformance(ComfyUISystemPerformance performance) {
        handlers.forEach(handler -> handler.systemPerformance(performance));
    }
}
