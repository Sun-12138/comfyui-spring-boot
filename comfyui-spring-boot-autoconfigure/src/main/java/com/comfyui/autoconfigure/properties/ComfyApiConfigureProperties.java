package com.comfyui.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ComfyUI服务参数配置
 * @author Sun_12138
 */
@Data
@ConfigurationProperties(prefix = ComfyApiConfigureProperties.PREFIX)
public class ComfyApiConfigureProperties {

    /**
     * 配置前缀
     */
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
     * 会话id 英文数字或UUID
     */
    private String clientId;
    /**
     * 提交绘图任务时，如果Comfyui内部有非当前服务提交的任务时是否自动取消
     */
    private boolean autoClearTask = true;
}
