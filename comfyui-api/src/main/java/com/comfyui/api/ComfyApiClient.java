package com.comfyui.api;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.comfyui.api.exceptions.WorkFlowErrorException;
import com.comfyui.common.utils.JsonUtils;
import com.comfyui.common.entity.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;

/**
 * ComfyUI客户端
 *
 * @author Sun_12138
 */
public class ComfyApiClient implements IComfyApi {

    /**
     * ComfyUI地址
     */
    private final String serverUri;

    /**
     * 会话id
     */
    private final String clientId;

    /**
     * @param serverUri comfyUI服务地址
     * @param clientId  会话id
     */
    public ComfyApiClient(URI serverUri, String clientId) {
        this.serverUri = serverUri.toString();
        this.clientId = clientId;
    }

    /**
     * api: /prompt<br>
     * 提交图片生成任务
     *
     * @param taskId 自定义的任务id
     * @param flow   工作流
     * @return ComfyUI内部任务id
     */
    @Override
    public String submitDrawTask(String taskId, ComfyWorkFlow flow) {
        String param = String.format("{\"client_id\": \"%s\", \"prompt\": %s}", clientId, flow);
        HttpRequest request = HttpRequest.post(serverUri + "prompt")
                .body(param, "application/json")
                .timeout(2000);
        //提交任务
        String resp = execHttpRequest(request);
        JsonNode jsonNode = JsonUtils.toJsonNode(resp);
        JsonNode taskIdNode = jsonNode.get("prompt_id");
        if (taskIdNode == null) {
            //任务提交错误 工作流节点出现错误
            throw new WorkFlowErrorException("工作流节点错误");
        }
        return taskIdNode.asText();
    }

    /**
     * api: /history<br>
     * 获得全部历史任务信息
     *
     * @return 历史任务信息 key为任务id value为任务信息
     */
    @Override
    public Map<String, ComfyTaskHistoryInfo> getTaskInfo() {
        HttpRequest request = HttpRequest.get(serverUri + "history")
                .timeout(2000);
        String historyInfo = execHttpRequest(request);
        return JsonUtils.toMapObject(historyInfo, ComfyTaskHistoryInfo.class);
    }

    /**
     * api: /history/{comfyTaskId}<br>
     * 获得某一个任务信息
     *
     * @param comfyTaskId 任务id
     * @return 历史任务信息
     */
    @Override
    public ComfyTaskHistoryInfo getTaskInfoById(String comfyTaskId) {
        HttpRequest request = HttpRequest.get(serverUri + "history/" + comfyTaskId)
                .timeout(2000);
        String historyInfo = execHttpRequest(request);
        JsonNode taskNode = JsonUtils.toJsonNode(historyInfo).get(comfyTaskId);
        return JsonUtils.toObject(taskNode, ComfyTaskHistoryInfo.class);
    }

    /**
     * api: /queue
     * 获得当前队列状态
     *
     * @return 当前队列状态
     */
    @Override
    public ComfyTaskQueueStatus getQueueStatus() {
        HttpRequest request = HttpRequest.get(serverUri + "queue")
                .timeout(2000);
        String queueStatusInfo = execHttpRequest(request);
        return JsonUtils.toObject(queueStatusInfo, ComfyTaskQueueStatus.class);
    }

    /**
     * api: /queue<br>
     * 删除一个正在等待的绘画任务
     *
     * @param comfyTaskId comfyUI内部任务id
     */
    @Override
    public void cancelDrawTask(String comfyTaskId) {
        String param = String.format("{\"delete\":[\"%s\"]}", comfyTaskId);
        HttpRequest request = HttpRequest.post(serverUri + "queue")
                .body(param, "application/json")
                .timeout(2000);
        execHttpRequest(request);
    }

    /**
     * api: /interrupt<br>
     * 取消当前运行的任务
     */
    @Override
    public void cancelRunningTask() {
        HttpRequest request = HttpRequest.post(serverUri + "interrupt")
                .timeout(2000);
        execHttpRequest(request);
    }

    /**
     * api: /system_stats<br>
     * 获取系统运行信息
     *
     * @return 系统运行环境 硬件设备
     */
    @Override
    public ComfySystemEnvironment getSystemDeviceInfo() {
        HttpRequest request = HttpRequest.get(serverUri + "system_stats")
                .timeout(2000);
        String systemStatsInfo = execHttpRequest(request);
        return JsonUtils.toObject(systemStatsInfo, ComfySystemEnvironment.class);
    }

    /**
     * api: /upload/image<br>
     * 上传图片到ComfyUI服务器
     *
     * @param file 图片对象
     * @return 上传后的图片信息
     */
    @Override
    public ComfyTaskImage uploadImage(File file) {
        HttpRequest request = HttpRequest.post(serverUri + "upload/image")
                .form("file", file)
                .timeout(2000);
        String resp = execHttpRequest(request);
        return JsonUtils.toObject(resp, ComfyTaskImage.class);
    }

    /**
     * api: /view
     * 获取输入的图片文件
     *
     * @param imageName 图片文件名
     * @return 图片文件二进制数组
     */
    @Override
    public byte[] getInputImageFile(String imageName) {
        return getImageFile(imageName, "input");
    }

    /**
     * api: /view
     * 获取生成的图片文件
     *
     * @param imageName 图片文件名
     * @return 图片文件二进制数组
     */
    @Override
    public byte[] getOutputImageFile(String imageName) {
        return getImageFile(imageName, "output");
    }

    /**
     * 获取图片文件
     *
     * @param imageName 图片名
     * @param folder    图片所在文件夹
     * @return 图片文件二进制数组
     */
    private byte[] getImageFile(String imageName, String folder) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            UrlBuilder builder = UrlBuilder.of(serverUri)
                    .addQuery("filename", imageName)
                    .addQuery("type", "input")
                    .addQuery("type", folder)
                    .addPath("/view");
            HttpUtil.download(builder.build(), out, false);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行request 并自动关闭response
     *
     * @param request request对象
     * @return response中的body
     */
    private String execHttpRequest(HttpRequest request) {
        try (HttpResponse response = request.execute()) {
            return response.body();
        }
    }
}
