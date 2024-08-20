package com.comfyui.monitor.rabbitmq;

import com.comfyui.annotation.enums.TaskProcessType;
import com.comfyui.common.process.*;
import com.comfyui.monitor.message.TaskProcessSender;
import lombok.RequiredArgsConstructor;

/**
 * 任务进度监听者接口
 *
 * @author Sun_12138
 */
@RequiredArgsConstructor
public abstract class BaseTaskProcessMonitor {

    /**
     * 任务进度发布者
     */
    private final TaskProcessSender processSender;

    /**
     * 发布任务进度
     *
     * @param type 进度消息类型
     * @param info 消息内容
     */
    public void publishTaskProcess(TaskProcessType type, IComfyTaskProcess info) {
        switch (type) {
            case START:
                ComfyTaskStart startInfo = (ComfyTaskStart) info;
                processSender.taskStart(startInfo);
                break;
            case PROGRESS:
                ComfyTaskNodeProgress progressInfo = (ComfyTaskNodeProgress) info;
                processSender.taskNodeProgress(progressInfo);
                break;
            case PREVIEW:
                ComfyTaskProgressPreview previewInfo = (ComfyTaskProgressPreview) info;
                processSender.taskProgressPreview(previewInfo);
                break;
            case OUTPUT:
                ComfyTaskOutput outputInfo = (ComfyTaskOutput) info;
                processSender.taskOutput(outputInfo);
                break;
            case COMPLETE:
                ComfyTaskComplete completeInfo = (ComfyTaskComplete) info;
                processSender.taskComplete(completeInfo);
                break;
            case ERROR:
                ComfyTaskError errorInfo = (ComfyTaskError) info;
                processSender.taskError(errorInfo);
                break;
            case NUMBER_UPDATE:
                ComfyTaskNumber numberInfo = (ComfyTaskNumber) info;
                processSender.taskNumberUpdate(numberInfo);
                break;
            case SYSTEM_PERFORMANCE:
                ComfySystemPerformance systemPerformance = (ComfySystemPerformance) info;
                processSender.systemPerformance(systemPerformance);
                break;
            default:
        }
    }
}
