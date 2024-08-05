package com.comfyui;

import cn.hutool.core.net.url.UrlBuilder;
import com.comfyui.client.ComfyWebSocketClient;
import com.comfyui.client.ComfyClient;
import com.comfyui.client.handler.strategy.ComfyWebSocketHandleStrategyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Sun_12138
 */
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.comfyui")
@EnableConfigurationProperties(ComfyClientProperties.class)
public class ComfyClientAutoConfiguration {

    private final ComfyClientProperties serversProperties;

    /**
     * 默认ComfyUI WebSocket连接客户端
     */
    @Bean
    public ComfyWebSocketClient comfyWebSocketClient(ComfyWebSocketHandleStrategyMapper strategyMapper, ComfyClient comfyClient) throws URISyntaxException {
        String host = serversProperties.getHost();
        int port = serversProperties.getPort();
        String clientId = serversProperties.getClientId();
        URI wsServerUri = new URI(String.format("ws://%s:%d/ws?clientId=%s", host, port, clientId));
        return new ComfyWebSocketClient(wsServerUri, comfyClient, strategyMapper);
    }

    /**
     * ComfyUI客户端
     */
    @Bean
    public ComfyClient comfyClient() {
        String host = serversProperties.getHost();
        int port = serversProperties.getPort();
        String clientId = serversProperties.getClientId();
        URI serverUri = new UrlBuilder()
                .setHost(host)
                .setPort(port)
                .toURI();
        return new ComfyClient(serverUri, clientId);
    }
}
