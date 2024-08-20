package com.comfyui.monitor.handler;

import com.comfyui.common.entity.ComfyTaskImage;
import com.comfyui.common.entity.ComfyWorkFlow;
import com.comfyui.common.entity.ComfyWorkFlowNode;
import com.comfyui.monitor.ComfyWebSocketClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Sun_12138
 */
@Getter
@RequiredArgsConstructor
public class TaskProcessContext {

    /**
     * 任务id
     */
    private final String taskId;

    /**
     * comfyUI内部任务id
     */
    private final String comfyTaskId;

    /**
     * comfyui websocket客户端
     */
    private final ComfyWebSocketClient websocketClient;

    /**
     * 工作流对象
     */
    private final ComfyWorkFlow workFlow;

    /**
     * 输出的图片
     */
    private final List<ComfyTaskImage> outputImages;


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
