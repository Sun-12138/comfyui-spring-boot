package com.comfyui;

import cn.hutool.core.net.url.UrlBuilder;
import com.comfyui.client.ComfyUIWebSocketClient;
import com.comfyui.client.ComfyUIClient;
import com.comfyui.client.handler.strategy.ComfyUIWebSocketHandleStrategyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.comfyui")
@EnableConfigurationProperties(ComfyUIClientProperties.class)
public class ComfyUIClientAutoConfiguration {

    private final ComfyUIClientProperties serversProperties;

    /**
     * 默认ComfyUI WebSocket连接客户端
     */
    @Bean
    public ComfyUIWebSocketClient comfyUIWebSocketClient(ComfyUIWebSocketHandleStrategyMapper strategyMapper, ComfyUIClient comfyUIClient) throws URISyntaxException {
        String host = serversProperties.getHost();
        int port = serversProperties.getPort();
        String clientId = serversProperties.getClient_id();
        URI wsServerUri = new URI(String.format("ws://%s:%d/ws?clientId=%s", host, port, clientId));
        return new ComfyUIWebSocketClient(wsServerUri, comfyUIClient, strategyMapper);
    }

    /**
     * ComfyUI客户端
     */
    @Bean
    public ComfyUIClient comfyUIClient() {
        String host = serversProperties.getHost();
        int port = serversProperties.getPort();
        String clientId = serversProperties.getClient_id();
        URI serverUri = new UrlBuilder()
                .setHost(host)
                .setPort(port)
                .toURI();
        return new ComfyUIClient(serverUri, clientId);
    }
}
