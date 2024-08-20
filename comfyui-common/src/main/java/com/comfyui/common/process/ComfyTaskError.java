package com.comfyui.common.process;

import com.comfyui.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 任务失败
 *
 * @author Sun_12138
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ComfyTaskError implements IComfyTaskProcess {

    /**
     * 自定义任务id
     */
    private String taskId;

    /**
     * comfyUI内部任务id
     */
    private String comfyTaskId;

    /**
     * 错误信息
     */
    private String errorInfo;

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
