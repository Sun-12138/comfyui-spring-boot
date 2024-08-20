package com.comfyui.common.process;

import com.comfyui.common.utils.JsonUtils;
import com.comfyui.common.entity.ComfyTaskImage;
import com.comfyui.common.entity.ComfyWorkFlowNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 任务输出的图片
 *
 * @author Sun_12138
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ComfyTaskOutput implements IComfyTaskProcess {

    /**
     * 自定义任务id
     */
    private String taskId;

    /**
     *  comfyUI内部任务id
     */
    private String comfyTaskId;

    /**
     * 输出的图片
     */
    private List<ComfyTaskImage> outputImages;

    /**
     *当前节点id 例如 1, 2, 3
     */
    private ComfyWorkFlowNode node;

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
