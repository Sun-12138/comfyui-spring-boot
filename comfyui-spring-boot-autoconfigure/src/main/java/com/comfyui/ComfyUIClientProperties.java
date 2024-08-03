package com.comfyui;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * ComfyUI服务参数配置
 */
@Data
@ConfigurationProperties(prefix = ComfyUIClientProperties.PREFIX)
public class ComfyUIClientProperties {

    public static final String PREFIX = "spring.comfyui";

    /**
     * ComfyUI地址 默认：<a href="http://127.0.0.1">127.0.0.1</a>
     */
    private String host = "127.0.0.1";
    /**
     * ComfyUI端口 默认8188
     */
    private int port = 8188;
    /**
     * 会话id 英文数字或UUID 默认随机生成
     */
    private String client_id = String.valueOf(UUID.randomUUID());
}
