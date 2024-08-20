package com.comfyui.autoconfigure;

import cn.hutool.core.collection.ListUtil;
import com.comfyui.api.ComfyApiClient;
import com.comfyui.autoconfigure.properties.ComfyApiConfigureProperties;
import com.comfyui.monitor.ComfyWebSocketClient;
import com.comfyui.monitor.enums.ComfyWebSocketMessageType;
import com.comfyui.monitor.handler.ComfyWebSocketMessageHandler;
import com.comfyui.monitor.handler.strategy.*;
import com.comfyui.monitor.message.TaskProcessSender;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sun_12138
 */
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.comfyui.monitor.handler.strategy")
@EnableConfigurationProperties(ComfyApiConfigureProperties.class)
public class ComfyWebSocketClientAutoConfigure {

    private final ComfyApiConfigureProperties serversProperties;

    /**
     * ws消息处理策略类
     *
     * @param systemPerformanceHandleStrategy 系统性能处理策略类
     * @param taskCacheHandleStrategy 任务缓存处理策略类
     * @param taskCompleteHandleStrategy 任务完成处理策略类
     * @param taskInterruptedHandleStrategy 任务取消或被中断处理策略类
     * @param taskNodeUpdateHandleStrategy 任务节点更新处理策略类
     * @param taskNumberUpdateHandleStrategy 任务节点个数更新处理策略类
     * @param taskOutputHandleStrategy 任务输出处理策略类
     * @param taskProgressHandleStrategy 任务进度处理策略类
     * @param taskStartHandleStrategy 任务开始处理策略类
     * @return ws消息处理策略类
     */
    @Bean
    public Map<ComfyWebSocketMessageType, IComfyWebSocketTextHandleStrategy> textStrategyMapper(
            SystemPerformanceHandleStrategy systemPerformanceHandleStrategy,
            TaskCacheHandleStrategy taskCacheHandleStrategy,
            TaskCompleteHandleStrategy taskCompleteHandleStrategy,
            TaskInterruptedHandleStrategy taskInterruptedHandleStrategy,
            TaskNodeUpdateHandleStrategy taskNodeUpdateHandleStrategy,
            TaskNumberUpdateHandleStrategy taskNumberUpdateHandleStrategy,
            TaskOutputHandleStrategy taskOutputHandleStrategy,
            TaskProgressHandleStrategy taskProgressHandleStrategy,
            TaskStartHandleStrategy taskStartHandleStrategy
    ) {
        Map<ComfyWebSocketMessageType, IComfyWebSocketTextHandleStrategy> textStrategyMapper = new HashMap<>(9);
        //任务开始
        textStrategyMapper.put(ComfyWebSocketMessageType.EXECUTION_START, taskStartHandleStrategy);
        //缓存任务
        textStrategyMapper.put(ComfyWebSocketMessageType.EXECUTION_CACHED, taskCacheHandleStrategy);
        //任务进度更新
        textStrategyMapper.put(ComfyWebSocketMessageType.PROGRESS, taskProgressHandleStrategy);
        textStrategyMapper.put(ComfyWebSocketMessageType.EXECUTING, taskNodeUpdateHandleStrategy);
        //任务结果输出
        textStrategyMapper.put(ComfyWebSocketMessageType.EXECUTED, taskOutputHandleStrategy);
        //任务完成
        textStrategyMapper.put(ComfyWebSocketMessageType.COMPLETE, taskCompleteHandleStrategy);
        //任务取消或被中断
        textStrategyMapper.put(ComfyWebSocketMessageType.INTERRUPT, taskInterruptedHandleStrategy);
        //系统状态任务个数更新
        textStrategyMapper.put(ComfyWebSocketMessageType.TASK_NUMBER, taskNumberUpdateHandleStrategy);
        //系统性能信息
        textStrategyMapper.put(ComfyWebSocketMessageType.MONITOR, systemPerformanceHandleStrategy);
        return textStrategyMapper;
    }

    /**
     * @param processSender 任务进度发送器
     * @param textStrategyMapper ws消息处理策略类
     * @param taskProgressHandleStrategy 任务进度处理策略类
     * @return ws消息处理者
     */
    @Bean
    public ComfyWebSocketMessageHandler comfyWebSocketMessageHandler(
            TaskProcessSender processSender,
            Map<ComfyWebSocketMessageType, IComfyWebSocketTextHandleStrategy> textStrategyMapper,
            TaskProgressHandleStrategy taskProgressHandleStrategy) {
        return new ComfyWebSocketMessageHandler(processSender, textStrategyMapper, ListUtil.of(taskProgressHandleStrategy));
    }

    /**
     * 默认ComfyUI WebSocket连接客户端
     *
     * @param messageHandler ws消息处理策略类
     * @param comfyClient    ComfyUI客户端
     * @return ComfyUI WebSocket客户端
     * @throws URISyntaxException URL解析异常
     */
    @Bean
    public ComfyWebSocketClient comfyWebSocketClient(ComfyWebSocketMessageHandler messageHandler, ComfyApiClient comfyClient) throws URISyntaxException {
        String host = serversProperties.getHost();
        int port = serversProperties.getPort();
        String clientId = serversProperties.getClientId();
        URI wsServerUri = new URI(String.format("ws://%s:%d/ws?clientId=%s", host, port, clientId));
        boolean autoClear = serversProperties.isAutoClearTask();
        return new ComfyWebSocketClient(wsServerUri, comfyClient, messageHandler, autoClear);
    }
}
