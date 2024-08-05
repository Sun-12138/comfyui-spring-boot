package com.comfyui.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ComfyUI图片对象
 * @author Sun_12138
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComfyTaskImage {
    /**
     * 图片名
     */
    @JsonProperty("filename")
    private String fileName;

    /**
     * 图片存放位置的文件夹
     */
    @JsonProperty("subfolder")
    private String subFolder;

    /**
     * 图片存放位置的文件夹
     */
    @JsonProperty("type")
    private String folder;

    /**
     * 可能时格式吧 例：WEBG
     */
    private String preview;

    /**
     * 应该是图片颜色通道 例：rgb
     */
    private String channel;
}
