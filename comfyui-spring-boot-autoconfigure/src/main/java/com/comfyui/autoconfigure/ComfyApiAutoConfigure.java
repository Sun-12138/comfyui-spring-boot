package com.comfyui.autoconfigure;

import cn.hutool.core.net.url.UrlBuilder;
import com.comfyui.api.ComfyApiClient;
import com.comfyui.autoconfigure.properties.ComfyApiConfigureProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * @author Sun_12138
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ComfyApiConfigureProperties.class)
public class ComfyApiAutoConfigure {

    private final ComfyApiConfigureProperties serversProperties;


    /**
     * ComfyUI客户端
     *
     * @return ComfyUI客户端
     */
    @Bean
    public ComfyApiClient comfyClient() {
        String host = serversProperties.getHost();
        int port = serversProperties.getPort();
        String clientId = serversProperties.getClientId();
        URI serverUri = new UrlBuilder()
                .setHost(host)
                .setPort(port)
                .toURI();
        return new ComfyApiClient(serverUri, clientId);
    }
}
