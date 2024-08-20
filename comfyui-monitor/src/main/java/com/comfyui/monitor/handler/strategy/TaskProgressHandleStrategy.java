package com.comfyui.monitor.handler.strategy;

import com.comfyui.common.process.ComfyTaskNodeProgress;
import com.comfyui.common.process.ComfyTaskProgressPreview;
import com.comfyui.monitor.enums.ComfyWebSocketMessageType;
import com.comfyui.monitor.handler.TaskProcessContext;
import com.comfyui.monitor.message.TaskProcessSender;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 绘图任务工作流节点进度更新
 *
 * @author Sun_12138
 */
@Component
public class TaskProgressHandleStrategy implements IComfyWebSocketTextHandleStrategy, IComfyWebSocketByteHandleStrategy {

    /**
     * 处理消息
     *
     * @param msgType       消息类型
     * @param dataNode      消息内容
     * @param processSender 消息进度发送者
     * @param ctx           上下文任务状态
     */
    @Override
    public void handleMessage(ComfyWebSocketMessageType msgType, JsonNode dataNode, TaskProcessSender processSender, TaskProcessContext ctx) {
        String nodeId = dataNode.get("node").asText();
        //当消息类型为progress时有真实进度
        int current = dataNode.get("value").asInt();
        int max = dataNode.path("max").asInt();
        //计算进度百分比
        int percent = 100;
        if (max!= 0) {
            BigDecimal bd1 = new BigDecimal(current);
            BigDecimal bd2 = new BigDecimal(max);
            percent = bd1.divide(bd2, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).intValue();
        }
        processSender.taskNodeProgress(new ComfyTaskNodeProgress(ctx.getTaskId(), ctx.getComfyTaskId(), current, max, percent, ctx.getWorkFlowNode(nodeId)));
    }

    /**
     * 处理二进制消息
     *
     * @param bytes         二进制数组
     * @param processSender 消息进度发送者
     * @param ctx           上下文任务状态
     */
    @Override
    public void handleByte(byte[] bytes, TaskProcessSender processSender, TaskProcessContext ctx) {
        processSender.taskProgressPreview(new ComfyTaskProgressPreview(ctx.getTaskId(), ctx.getComfyTaskId(), bytes));
    }
}
