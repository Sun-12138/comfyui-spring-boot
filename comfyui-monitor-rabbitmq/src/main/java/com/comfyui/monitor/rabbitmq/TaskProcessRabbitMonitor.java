package com.comfyui.monitor.rabbitmq;

import com.comfyui.common.utils.JsonUtils;
import com.comfyui.monitor.message.TaskProcessSender;
import com.comfyui.queue.rabbitmq.message.DrawingTaskProcessMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

/**
 * @author Sun_12138
 */
@Slf4j
public class TaskProcessRabbitMonitor extends BaseTaskProcessMonitor {

    /**
     * @param sender 任务进度发布者
     */
    public TaskProcessRabbitMonitor(TaskProcessSender sender) {
        super(sender);
    }

    /**
     * 接收到绘图任务进度
     *
     * @param msg         消息
     * @param channel     rabbitmq channel
     * @param amqpMessage rabbitmq message
     * @throws IOException rabbitmq io exception
     */
    @RabbitListener(queues = "#{monitorQueueNameHolder.queueName}")
    public void receiveTask(String msg, Channel channel, Message amqpMessage) throws IOException {
        DrawingTaskProcessMessage msgObj = JsonUtils.toObject(msg, DrawingTaskProcessMessage.class);
        //发布消息
        publishTaskProcess(msgObj.getType(), msgObj.getInfo());
        //消费该消息
        channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
    }

}
