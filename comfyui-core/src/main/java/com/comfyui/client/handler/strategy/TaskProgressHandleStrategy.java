package com.comfyui.client.handler.strategy;

import com.comfyui.client.enums.ComfyUITaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfyUITaskNodeProgress;
import com.comfyui.entity.ComfyUITaskProgressPreview;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * 绘图任务工作流节点进度更新
 */
@Component
public class TaskProgressHandleStrategy implements IComfyUIWebSocketTextHandleStrategy, IComfyUIWebSocketByteHandleStrategy {

    /**
     * 处理消息
     *
     * @param msgType       消息类型
     * @param dataNode      消息内容
     * @param processSender 任务处理结果接收对象
     * @param ctx           上下文任务状态
     */
    @Override
    public void handleMessage(ComfyUITaskMsgType msgType, JsonNode dataNode, TaskProcessSender processSender, TaskHandlerStrategyContext ctx) {
        String nodeId = dataNode.get("node").asText();
        //当消息类型为progress时有真实进度
        int current = dataNode.get("value").asInt();
        int max = dataNode.path("max").asInt();
        //计算进度百分比
        double percent = Double.parseDouble(String.format("%.2f", (double) current / (double) max));
        processSender.taskNodeProgress(new ComfyUITaskNodeProgress(ctx.taskId(), ctx.comfyUITaskId(), current, max, percent, nodeId));
    }

    /**
     * 处理二进制消息
     *
     * @param bytes  二进制数组
     * @param caller 任务处理结果接收对象
     * @param ctx    上下文任务状态
     */
    @Override
    public void handleByte(byte[] bytes, TaskProcessSender caller, TaskHandlerStrategyContext ctx) {
        if (ctx != null) {
            caller.taskProgressPreview(new ComfyUITaskProgressPreview(ctx.taskId(), ctx.comfyUITaskId(), bytes));
        }
    }
}
