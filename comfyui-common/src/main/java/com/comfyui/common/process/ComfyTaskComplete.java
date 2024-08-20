package com.comfyui.common.process;

import com.comfyui.common.utils.JsonUtils;
import com.comfyui.common.entity.ComfyTaskImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 任务完成
 *
 * @author Sun_12138
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ComfyTaskComplete implements IComfyTaskProcess {

    /**
     * 自定义任务id
     */
    private String taskId;

    /**
     * comfyUI内部任务id
     */
    private String comfyTaskId;

    /**
     * 任务输出的图片
     */
    private List<ComfyTaskImage> images;

    /**
     * 是否为缓存结果
     */
    private boolean cache;

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
