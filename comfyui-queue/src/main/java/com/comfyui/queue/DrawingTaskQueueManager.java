package com.comfyui.queue;

import com.comfyui.common.DrawingTaskExecutor;
import com.comfyui.common.DrawingTaskInfo;
import com.comfyui.common.IDrawingTaskSubmit;
import com.comfyui.node.ComfyWorkFlow;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 绘图任务队列管理者
 * @author Sun_12138
 */
@Slf4j
public class DrawingTaskQueueManager implements IDrawingTaskSubmit {
    private final ExecutorService executorService;

    /**
     * 任务队列
     */
    private final LinkedBlockingQueue<DrawingTaskInfo> taskQueue;

    /**
     * 绘图任务执行者
     */
    private final DrawingTaskExecutor taskExecutor;

    public DrawingTaskQueueManager(DrawingTaskExecutor taskExecutor) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.taskQueue = new LinkedBlockingQueue<>();
        this.taskExecutor = taskExecutor;
        this.startTaskProcessing();
    }

    /**
     * 提交任务
     *
     * @param taskInfo 任务信息
     * @return 是否成功
     */
    @Override
    public boolean submit(DrawingTaskInfo taskInfo) {
        return taskQueue.offer(taskInfo);
    }

    /**
     * 开启任务线程
     */
    private void startTaskProcessing() {
        executorService.execute(this::processTaskQueue);
    }

    /**
     * 处理任务队列
     */
    private void processTaskQueue() {
        while (true) {
            try {
                synchronized (taskQueue) {
                    DrawingTaskInfo taskInfo = taskQueue.peek();
                    if (taskInfo == null) {
                        continue;
                    }
                    String taskId = taskInfo.taskId();
                    ComfyWorkFlow flow = taskInfo.flow();
                    long timeout = taskInfo.timeout();
                    TimeUnit unit = taskInfo.unit();
                    //执行绘图任务
                    taskExecutor.execDrawingTask(taskId, flow, timeout, unit);
                    //出队该任务
                    taskQueue.take();
                }
            } catch (InterruptedException e) {
                // 重置中断状态
                Thread.currentThread().interrupt();
                //重启线程
                this.startTaskProcessing();
                log.error("绘图任务线程发生中断, 重启线程");
                break;
            }
        }
    }
}
