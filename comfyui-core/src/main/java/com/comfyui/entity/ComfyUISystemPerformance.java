package com.comfyui.entity;

import com.comfyui.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * WebSocket当前系统负载
 *
 * @param deviceType 设备类型 例：cuda、cpu
 * @param cpu        CPU状态
 * @param gpus       全部显卡状态
 */
@JsonDeserialize(using = ComfyUISystemPerformance.Deserializer.class)
public record ComfyUISystemPerformance(String deviceType,
                                       ComfyUISystemPerformance.CpuPerformance cpu,
                                       List<GpuPerformance> gpus) implements IComfyUITaskProcess {
    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
    /**
     * CPU性能状态
     *
     * @param cpuUtilization CPU使用率
     * @param ramTotal       总内存容量 单位B
     * @param ramUsed        已用内存容量 单位B
     * @param ramUsedPercent 内存使用率
     * @param hddTotal       总硬盘空间 单位B
     * @param hddUsed        已使用硬盘空间 单位B
     * @param hddUsedPercent 硬盘使用率
     */
    public record CpuPerformance(@JsonProperty("cpu_utilization") Double cpuUtilization,
                                 @JsonProperty("ram_total") Long ramTotal, @JsonProperty("ram_used") Long ramUsed,
                                 @JsonProperty("ram_used_percent") Double ramUsedPercent,
                                 @JsonProperty("hdd_total") Long hddTotal, @JsonProperty("hdd_used") Long hddUsed,
                                 @JsonProperty("hdd_used_percent") Double hddUsedPercent) {
        @Override
        public String toString() {
            return JsonUtils.toJsonString(this);
        }
    }

    /**
     * 显卡性能状态
     *
     * @param gpuUtilization  GPU使用率
     * @param vramTotal       总显存容量
     * @param vramUsed        已使用显存
     * @param vramUsedPercent 显示使用率
     */
    public record GpuPerformance(@JsonProperty("gpu_utilization") Double gpuUtilization,
                                 @JsonProperty("vram_total") Long vramTotal, @JsonProperty("vram_used") Long vramUsed,
                                 @JsonProperty("vram_used_percent") Double vramUsedPercent) {
        @Override
        public String toString() {
            return JsonUtils.toJsonString(this);
        }
    }

    /**
     * 自定义WorkFLowData Json解析器
     */
    protected static class Deserializer extends JsonDeserializer<ComfyUISystemPerformance> {
        @Override
        public ComfyUISystemPerformance deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
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

            return new ComfyUISystemPerformance(deviceType, cpuData, gpus);
        }
    }
}
