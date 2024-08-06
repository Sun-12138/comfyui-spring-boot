package com.comfyui.entity;

import com.comfyui.node.ComfyWorkFlowNode;
import com.comfyui.utils.JsonUtils;

/**
 * 当前执行的节点和节点执行进度
 *
 * @author Sun_12138
 * @param taskId          自定义任务id
 * @param comfyTaskId   comfyUI内部任务id
 * @param currentProgress 当前进度
 * @param maxProgress     总进度
 * @param percent         进度百分比
 * @param node          当前任务节点id 例如 1, 2, 3
 */
public record ComfyTaskNodeProgress(String taskId, String comfyTaskId, int currentProgress, int maxProgress,
                                    double percent, ComfyWorkFlowNode node) implements IComfyTaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
