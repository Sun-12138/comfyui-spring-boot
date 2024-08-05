package com.comfyui.common;

import com.comfyui.TaskErrorException;
import com.comfyui.TaskTimeoutException;
import com.comfyui.client.ComfyWebSocketClient;
import com.comfyui.client.enums.TaskProcessType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.client.handler.process.ITaskProcessSubjectSubscriber;
import com.comfyui.client.handler.process.TaskProcessInfo;
import com.comfyui.client.handler.process.TaskProcessPublisher;
import com.comfyui.entity.ComfyTaskError;
import com.comfyui.entity.IComfyTaskProcess;
import com.comfyui.node.ComfyWorkFlow;
import com.comfyui.client.ComfyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 绘图任务执行者
 * @author Sun_12138
 */
@Component
@RequiredArgsConstructor
public class DrawingTaskExecutor {

    /**
     * 用于订阅任务进度
     */
    private final TaskProcessPublisher publisher;

    /**
     * 发送任务进度者
     */
    private final TaskProcessSender taskProcessSender;

    /**
     * ComfyUI服务端
     */
    private final ComfyClient comfyClient;

    /**
     * ComfyUI WebSocket任务进度监听
     */
    private final ComfyWebSocketClient comfyWebSocketClient;

    /**
     * 执行绘图任务
     *
     * @param taskInfo 任务信息
     */
    public void execDrawingTask(DrawingTaskInfo taskInfo) {
        execDrawingTask(taskInfo.taskId(), taskInfo.flow(), taskInfo.timeout(), taskInfo.unit());
    }

    /**
     * 执行绘图任务
     *
     * @param taskId  自定义的任务id
     * @param flow    工作流
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    public void execDrawingTask(String taskId, ComfyWorkFlow flow, long timeout, TimeUnit unit) {
        //注册任务进度观察者
        CountDownLatch latch = new CountDownLatch(1);
        //订阅任务结束事件
        TaskCompleteSubscriber subscriber = new TaskCompleteSubscriber(latch);
        //订阅完成和失败两个事件
        publisher.subscribe(taskId, TaskProcessType.COMPLETE, subscriber);
        publisher.subscribe(taskId, TaskProcessType.ERROR, subscriber);
        //提交绘图任务并开始监听任务进度
        String comfyTaskId = comfyWebSocketClient.startDrawingTask(taskId, flow);
        //此处进行阻塞，等待绘图任务完成
        try {
            if (!latch.await(timeout, unit)) {
                throw new TimeoutException();
            }
        } catch (InterruptedException e) {
            //发生错误则取消当前任务
            comfyClient.cancelRunningTask();
            //停止监听进度
            comfyWebSocketClient.stopDrawingTask();
            //发送任务失败信息
            taskProcessSender.taskError(new ComfyTaskError(taskId, comfyTaskId, "监听绘图任务线程被中断"));
            throw new TaskErrorException(taskId, "监听绘图任务线程被中断");
        } catch (TimeoutException e) {
            //发生错误则取消当前任务
            comfyClient.cancelRunningTask();
            //停止监听进度
            comfyWebSocketClient.stopDrawingTask();
            //发送任务失败信息
            taskProcessSender.taskError(new ComfyTaskError(taskId, comfyTaskId, "绘图任务超时"));
            throw new TaskTimeoutException(taskId);
        } finally {
            //退订消息
            publisher.unSubscribe(taskId, TaskProcessType.COMPLETE, subscriber);
            publisher.unSubscribe(taskId, TaskProcessType.ERROR, subscriber);
        }
    }

    /**
     * 订阅绘图任务完成者
     */
    private record TaskCompleteSubscriber(CountDownLatch latch) implements ITaskProcessSubjectSubscriber {

        @Override
        public <T extends IComfyTaskProcess> void notify(TaskProcessInfo<T> event) {
            latch.countDown();
        }
    }
}
