package com.comfyui.monitor.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * comfyui ws消息任务进度类型
 *
 * @author Sun_12138
 */
@Getter
public enum ComfyWebSocketMessageType {
    /**
     * 任务开始
     */
    EXECUTION_START("execution_start"),
    /**
     * 检查任务缓存
     */
    EXECUTION_CACHED("execution_cached"),
    /**
     * 当前任务节点更新
     */
    EXECUTING("executing"),
    /**
     * 当前运行的耗时节点执行进度更新
     */
    PROGRESS("progress"),
    /**
     * 任务完成 任务结果输出
     */
    EXECUTED("executed"),
    /**
     * 任务中断
     */
    INTERRUPT("execution_interrupted"),
    /**
     * 任务完成
     */
    COMPLETE("execution_success"),
    /**
     * 系统队列任务数量更新
     */
    TASK_NUMBER("status"),
    /**
     * 系统性能状态更新
     */
    MONITOR("crystools.monitor");
    /**
     * 消息json中的消息类型字符串
     */
    final String type;

    /**
     * type字段与枚举对象的映射表
     */
    private static final Map<String, ComfyWebSocketMessageType> ENUM_MAP;

    static {
        Map<String, ComfyWebSocketMessageType> map = new HashMap<>();
        for (ComfyWebSocketMessageType value : ComfyWebSocketMessageType.values()) {
            map.put(value.getType(), value);
        }
        ENUM_MAP = map;
    }

    ComfyWebSocketMessageType(String type) {
        this.type = type;
    }

    /**
     * 通过ws消息中的type获取对应的枚举类
     *
     * @param type 消息中的type字段
     * @return 对应的枚举类
     */
    public static ComfyWebSocketMessageType fromType(String type) {
        if (!ENUM_MAP.containsKey(type)) {
            throw new IllegalArgumentException("Unknown task process type: " + type);
        }
        return ENUM_MAP.get(type);
    }
}
