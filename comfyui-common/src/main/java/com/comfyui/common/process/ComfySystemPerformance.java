package com.comfyui.common.process;

import com.comfyui.common.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * WebSocket当前系统负载
 *
 * @author Sun_12138
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = ComfySystemPerformance.Deserializer.class)
public class ComfySystemPerformance implements IComfyTaskProcess {
    /**
     * 设备类型 例：cuda、cpu
     */
    private String deviceType;

    /**
     * CPU状态
     */
    private CpuPerformance cpu;

    /**
     * 全部显卡状态
     */
    private List<GpuPerformance> gpus;

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

    /**
     * CPU性能状态
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CpuPerformance {

        /**
         * CPU使用率
         */
        @JsonProperty("cpu_utilization")
        private Double cpuUtilization;

        /**
         * 总内存容量 单位B
         */
        @JsonProperty("ram_total")
        private Long ramTotal;

        /**
         * 已用内存容量 单位B
         */
        @JsonProperty("ram_used")
        private Long ramUsed;

        /**
         * 内存已使用率
         */
        @JsonProperty("ram_used_percent")
        private Double ramUsedPercent;

        /**
         * 总硬盘空间 单位B
         */
        @JsonProperty("hdd_total")
        private Long hddTotal;

        /**
         * 已使用硬盘空间 单位B
         */
        @JsonProperty("hdd_used")
        private Long hddUsed;

        /**
         * 硬盘已使用率
         */
        @JsonProperty("hdd_used_percent")
        private Double hddUsedPercent;

        @Override
        public String toString() {
            return JsonUtils.toJsonString(this);
        }
    }

    /**
     * 显卡性能状态
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GpuPerformance {

        /**
         * GPU使用率
         */
        @JsonProperty("gpu_utilization")
        private Double gpuUtilization;

        /**
         * 总显存容量
         */
        @JsonProperty("vram_total")
        private Long vramTotal;

        /**
         * 已使用显存
         */
        @JsonProperty("vram_used")
        private Long vramUsed;

        /**
         * 显存已使用率
         */
        @JsonProperty("vram_used_percent")
        private Double vramUsedPercent;

        @Override
        public String toString() {
            return JsonUtils.toJsonString(this);
        }
    }

    /**
     * 自定义WorkFLowData Json解析器
     */
    protected static class Deserializer extends JsonDeserializer<ComfySystemPerformance> {
        @Override
        public ComfySystemPerformance deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            JsonNode root = p.readValueAsTree();

            //处理设备类型
            String deviceType = root.get("device_type").asText();

            //处理CPU状态
            CpuPerformance cpuData = JsonUtils.toObject(root, CpuPerformance.class);

            //处理全部显卡状态
            List<GpuPerformance> gpus = new ArrayList<>();
            JsonNode gpusListNode = root.get("gpus");
            for (JsonNode gpuNode : gpusListNode) {
                GpuPerformance gpuPerformance = JsonUtils.toObject(gpuNode, GpuPerformance.class);
                gpus.add(gpuPerformance);
            }

            return new ComfySystemPerformance(deviceType, cpuData, gpus);
        }
    }
}
