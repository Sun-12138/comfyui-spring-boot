package com.comfyui.client.handler.strategy;

import com.comfyui.entity.ComfyTaskImage;
import com.comfyui.node.ComfyWorkFlow;
import com.comfyui.node.ComfyWorkFlowNode;

import java.util.List;

/**
 * @param taskId       任务id
 * @param comfyTaskId  comfyUI内部任务id
 * @param workFlow     工作流对象
 * @param outputImages 输出的图片
 * @author Sun_12138
 */
public record TaskHandlerStrategyContext(String taskId, String comfyTaskId, ComfyWorkFlow workFlow,
                                         List<ComfyTaskImage> outputImages) {

    /**
     * 获得工作流节点对象
     *
     * @param nodeId 节点id
     * @return 工作流节点对象
     */
    public ComfyWorkFlowNode getWorkFlowNode(String nodeId) {
        return workFlow.getNode(nodeId);
    }
}
