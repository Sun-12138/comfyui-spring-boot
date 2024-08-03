package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;

/**
 * 当前执行的节点和节点执行进度
 *
 * @param taskId          自定义任务id
 * @param comfyUITaskId   comfyUI内部任务id
 * @param currentProgress 当前进度
 * @param maxProgress     总进度
 * @param percent         进度百分比
 * @param nodeId          当前任务节点id 例如 1, 2, 3
 */
public record ComfyUITaskNodeProgress(String taskId, String comfyUITaskId, int currentProgress, int maxProgress,
                                      double percent, String nodeId) implements IComfyUITaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
