package com.comfyui.client;

import com.comfyui.entity.ComfySystemEnvironment;
import com.comfyui.entity.ComfyTaskImage;
import com.comfyui.node.ComfyWorkFlow;
import com.comfyui.node.info.WorkFlowNodeInfo;
import com.comfyui.entity.ComfyTaskHistoryInfo;
import com.comfyui.entity.ComfyTaskQueueStatus;

import java.io.File;
import java.util.Map;

/**
 * 定义ComfyUI接口方法
 * @author Sun_12138
 */
public interface IComfyClientApi {

    /**
     * api: /prompt<br>
     * 提交图片生成任务
     *
     * @param taskId 自定义的任务id
     * @param flow   工作流
     * @return ComfyUI内部任务id
     */
    String submitDrawTask(String taskId, ComfyWorkFlow flow);

    /**
     * api: /object_info<br>
     * 获得所有工作节点信息
     *
     * @return 所有节点信息 key为节点class value为节点信息
     */
    Map<String, WorkFlowNodeInfo> getAllNodeInfo();

    /**
     * api：/object_info/{node_class}<br>
     * 通过节点class获取某个节点信息
     *
     * @param nodeClass 节点class
     * @return 节点信息
     */
    WorkFlowNodeInfo getNodeInfoByClass(String nodeClass);

    /**
     * api: /history<br>
     * 获得全部历史任务信息
     *
     * @return 历史任务信息 key为任务id value为任务信息
     */
    Map<String, ComfyTaskHistoryInfo> getTaskInfo();

    /**
     * api: /history/{comfyTaskId}<br>
     * 获得某一个任务信息
     *
     * @param comfyTaskId comfyUI内部任务id
     * @return 历史任务信息
     */
    ComfyTaskHistoryInfo getTaskInfoById(String comfyTaskId);

    /**
     * api: /queue
     * 获得当前队列状态
     *
     * @return 当前队列状态
     */
    ComfyTaskQueueStatus getQueueStatus();

    /**
     * api: /queue<br>
     * 取消一个绘画任务
     *
     * @param comfyTaskId comfyUI内部任务id
     */
    void cancelDrawTask(String comfyTaskId);

    /**
     * api: /interrupt<br>
     * 取消当前运行的任务
     */
    void cancelRunningTask();

    /**
     * api: /system_stats<br>
     * 获取系统运行信息
     *
     * @return 系统运行环境 硬件设备
     */
    ComfySystemEnvironment getSystemDeviceInfo();

    /**
     * api: /upload/image<br>
     * 上传图片到ComfyUI服务器
     *
     * @param file 图片对象
     * @return 上传后的图片信息
     */
    ComfyTaskImage uploadImage(File file);

    /**
     * api: /view
     * 获取输入的图片文件
     *
     * @param imageName 图片文件名
     * @return 图片文件二进制数组
     */
    byte[] getInputImageFile(String imageName);

    /**
     * api: /view
     * 获取生成的图片文件
     *
     * @param imageName 图片文件名
     * @return 图片文件二进制数组
     */
    byte[] getOutputImageFile(String imageName);
}
