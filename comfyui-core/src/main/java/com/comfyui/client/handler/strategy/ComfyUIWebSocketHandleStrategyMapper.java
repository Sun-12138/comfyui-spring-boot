package com.comfyui.client.handler.strategy;

import com.comfyui.client.enums.ComfyUITaskMsgType;
import com.comfyui.client.handler.TaskProcessSender;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于映射进度消息类型与对应的处理策略类
 */
@Component
public class ComfyUIWebSocketHandleStrategyMapper {

    /**
     * key为 消息类型type value对应任务处理策略实现类
     */
    private final Map<ComfyUITaskMsgType, IComfyUIWebSocketTextHandleStrategy> msgStrategyMapper = new HashMap<>();

    /**
     * 消息处理对象
     */
    private final TaskProcessSender caller;

    private final ApplicationContext springContext;

    public ComfyUIWebSocketHandleStrategyMapper(TaskProcessSender caller, ApplicationContext springContext) {
        this.caller = caller;
        this.springContext = springContext;
        initStrategy();
    }

    private <T> T getBean(Class<T> cls) {
        return springContext.getBean(cls);
    }

    /**
     * 初始化策略模式实例
     */
    private void initStrategy() {
        //任务开始
        msgStrategyMapper.put(ComfyUITaskMsgType.EXECUTION_START, getBean(TaskStartHandleStrategy.class));
        //缓存任务
        msgStrategyMapper.put(ComfyUITaskMsgType.EXECUTION_CACHED, getBean(TaskCacheHandleStrategy.class));
        //任务进度更新
        msgStrategyMapper.put(ComfyUITaskMsgType.PROGRESS, getBean(TaskProgressHandleStrategy.class));
        msgStrategyMapper.put(ComfyUITaskMsgType.EXECUTING, getBean(TaskNodeUpdateHandleStrategy.class));
        //任务结果输出
        msgStrategyMapper.put(ComfyUITaskMsgType.EXECUTED, getBean(TaskOutputHandleStrategy.class));
        //任务完成
        msgStrategyMapper.put(ComfyUITaskMsgType.COMPLETE, getBean(TaskCompleteHandleStrategy.class));
        //任务取消或被中断
        msgStrategyMapper.put(ComfyUITaskMsgType.INTERRUPT, getBean(TaskInterruptedHandleStrategy.class));
        //系统状态任务个数更新
        msgStrategyMapper.put(ComfyUITaskMsgType.TASK_NUMBER, getBean(TaskNumberUpdateHandleStrategy.class));
        //系统性能信息
        msgStrategyMapper.put(ComfyUITaskMsgType.MONITOR, getBean(SystemPerformanceHandleStrategy.class));
    }

    /**
     * 处理消息
     *
     * @param msgType 消息类型
     * @param dataNode 消息内容
     * @param context  上下文任务状态
     */
    public void handleMessage(ComfyUITaskMsgType msgType, JsonNode dataNode, TaskHandlerStrategyContext context) {
        //获取对应的策略实现类
        IComfyUIWebSocketTextHandleStrategy strategy = msgStrategyMapper.get(msgType);
        if (strategy != null) {
            strategy.handleMessage(msgType, dataNode, caller, context);
        }
    }

    /**
     * 处理二进制消息
     *
     * @param bytes   二进制数组
     * @param context 上下文任务状态
     */
    public void handleByte(byte[] bytes, TaskHandlerStrategyContext context) {
        IComfyUIWebSocketByteHandleStrategy byteHandleStrategy = getBean(TaskProgressHandleStrategy.class);
        byteHandleStrategy.handleByte(bytes, caller, context);
    }
}
