package com.comfyui.rabbit;

import com.comfyui.client.enums.TaskProcessType;
import com.comfyui.client.handler.ITaskProcessReceiver;
import com.comfyui.entity.*;
import com.comfyui.utils.JsonUtils;
import com.comfyui.common.DrawingTaskExecutor;
import com.comfyui.common.DrawingTaskInfo;
import com.comfyui.common.IDrawingTaskSubmit;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class DrawingTaskRabbitMQService implements IDrawingTaskSubmit, ITaskProcessReceiver {

    private final AmqpTemplate amqpTemplate;

    /**
     * 绘图任务执行者
     */
    private final DrawingTaskExecutor taskPaintingExecutor;

    /**
     * 接收到绘图任务
     */
    @RabbitListener(queues = RabbitQueueConfig.TASK_QUEUE)
    public void receiveTask(String msg, Channel channel, Message amqpMessage) throws IOException {
        try {
            DrawingTaskInfoMQMessage msgObj = JsonUtils.toObject(msg, DrawingTaskInfoMQMessage.class);
            try {
                //执行绘图任务
                taskPaintingExecutor.execDrawingTask(msgObj.taskInfo());
            } catch (Exception ignored) {
            }
        } finally {
            //消费该消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        }
    }

    /**
     * 提交任务
     *
     * @param taskInfo 任务信息
     * @return 是否成功
     */
    @Override
    public boolean submit(DrawingTaskInfo taskInfo) {
        try {
            amqpTemplate.convertAndSend(RabbitQueueConfig.TASK_EXCHANGE, RabbitQueueConfig.TASK_ROUTING_KEY, new DrawingTaskInfoMQMessage(taskInfo).toString());
            return true;
        } catch (Exception e) {
            //发生错误 重试五次
            for (int i = 0; i < 5; i++) {
                try {
                    amqpTemplate.convertAndSend(RabbitQueueConfig.TASK_EXCHANGE, RabbitQueueConfig.TASK_ROUTING_KEY, new DrawingTaskInfoMQMessage(taskInfo).toString());
                } catch (Exception ignored) {
                    continue;
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 发送任务进度到mq
     *
     * @param processMessage 任务进度信息
     */
    private void sendTaskProgressToMonitorQueue(DrawingTaskProcessMQMessage processMessage) {
        amqpTemplate.convertAndSend(RabbitQueueConfig.TASK_MONITOR_EXCHANGE, "", processMessage.toString());
    }

    /**
     * 任务开始
     *
     * @param start 任务开始信息
     */
    @Override
    public void taskStart(ComfyUITaskStart start) {
        sendTaskProgressToMonitorQueue(new DrawingTaskProcessMQMessage(TaskProcessType.START, start));
    }

    /**
     * 当前执行的节点和节点执行进度
     *
     * @param progress 进度信息
     */
    @Override
    public void taskNodeProgress(ComfyUITaskNodeProgress progress) {
        sendTaskProgressToMonitorQueue(new DrawingTaskProcessMQMessage(TaskProcessType.PROGRESS, progress));
    }

    /**
     * 任务进度预览效果图
     *
     * @param preview 预览图信息
     */
    @Override
    public void taskProgressPreview(ComfyUITaskProgressPreview preview) {
        sendTaskProgressToMonitorQueue(new DrawingTaskProcessMQMessage(TaskProcessType.PREVIEW, preview));
    }

    /**
     * 任务输出的图片
     *
     * @param output 输出信息
     */
    @Override
    public void taskOutput(ComfyUITaskOutput output) {
        sendTaskProgressToMonitorQueue(new DrawingTaskProcessMQMessage(TaskProcessType.OUTPUT, output));
    }

    /**
     * 任务完成
     *
     * @param complete 任务完成信息
     */
    @Override
    public void taskComplete(ComfyUITaskComplete complete) {
        sendTaskProgressToMonitorQueue(new DrawingTaskProcessMQMessage(TaskProcessType.COMPLETE, complete));
    }

    /**
     * 任务失败
     *
     * @param error 任务错误信息
     */
    @Override
    public void taskError(ComfyUITaskError error) {
        sendTaskProgressToMonitorQueue(new DrawingTaskProcessMQMessage(TaskProcessType.ERROR, error));
    }

    /**
     * 绘图队列任务个数更新
     *
     * @param taskNumber 队列任务信息
     */
    @Override
    public void taskNumberUpdate(ComfyUITaskNumber taskNumber) {
    }

    /**
     * 当前系统负载
     *
     * @param performance 系统状态
     */
    @Override
    public void systemPerformance(ComfyUISystemPerformance performance) {
    }
}
