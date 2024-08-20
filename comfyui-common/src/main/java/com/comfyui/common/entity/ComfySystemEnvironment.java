package com.comfyui.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 系统统计信息
 * @author Sun_12138
 */
@Getter
@AllArgsConstructor
public class ComfySystemEnvironment {
    /**
     * 系统运行环境
     */
    @JsonProperty("system")
    private SystemEnvironment systemEnvironment;
    /**
     * 系统显卡设备
     */
    private List<Devices> devices;

    /**
     * 系统环境
     */
    @Data
    private static class SystemEnvironment {
        /**
         * 系统版本
         */
        private String os;
        /**
         * Python运行环境
         */
        @JsonProperty("python_version")
        private String pythonVersion;
        /**
         * 百度翻译: 嵌入式Python
         */
        @JsonProperty("embedded_python")
        private Boolean embeddedPython;
    }

    /**
     * 设备信息
     */
    @Data
    private static class Devices {
        /**
         * 设备名(显卡名)
         */
        private String name;
        /**
         * 设备类型 cuda or cpu
         */
        private String type;
        /**
         * 设备序号 0开始
         */
        private Integer index;
        /**
         * 总显存
         */
        @JsonProperty("vram_total")
        private Long vramTotal;
        /**
         * 空余显存
         */
        @JsonProperty("vram_free")
        private Long vramFree;
        /**
         * 可能是sd可使用显存？
         */
        @JsonProperty("vram_used")
        private Long torchVramTotal;
        /**
         * 可能是sd使用空余显存？
         */
        @JsonProperty("vram_used_free")
        private Long torchVramFree;
    }
}
