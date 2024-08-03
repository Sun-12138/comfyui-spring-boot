package com.comfyui.client.handler.strategy;

import com.comfyui.client.enums.ComfyUITaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.comfyui.entity.ComfyUITaskNodeProgress;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 任务执行节点更新
 */
@Component
public class TaskNodeUpdateHandleStrategy implements IComfyUIWebSocketTextHandleStrategy {

    private ComfyUIWebSocketHandleStrategyMapper strategyMapper;

    @Lazy
    @Autowired
    public void setStrategyMapper(ComfyUIWebSocketHandleStrategyMapper strategyMapper) {
        this.strategyMapper = strategyMapper;
    }

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
        //判断是否为任务完成信息
        if (Objects.equals(msgType, ComfyUITaskMsgType.EXECUTING) && dataNode.get("node") instanceof NullNode) {
            //调用任务完成的处理策略
            strategyMapper.handleMessage(ComfyUITaskMsgType.COMPLETE, dataNode, ctx);
            return;
        }
        //为节点更新信息
        String nodeId = dataNode.get("node").asText();
        //当前消息没有真实进度 使用虚假进度
        processSender.taskNodeProgress(new ComfyUITaskNodeProgress(ctx.taskId(), ctx.comfyUITaskId(), 0, 0, 0.0, nodeId));
    }
}
