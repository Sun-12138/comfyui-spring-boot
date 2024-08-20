# spring-boot-comfyui

> 支持SpringBoot调用ComfyUI服务API、提交绘图任务到队列、实时获取任务进度，支持单机和集群使用

## 支持的队列类型

-   普通阻塞队列
-   RabbitMQ
-   自定义任务提交(后续支持)

## 版本要求

Java >= 8

ComfyUI版本：2024-08-13 03:03:33(建议不低于该版本)

## 前置知识

spring-boot-comfyui提交一个任务会有两种任务id

- taskId

  提交任务时需要两个参数，一个时自行定义的taskId，另一个是工作流，taskId最终会与提交工作流到ComfyUI服务产生的prompt_id(项目中参数名为comfyTaskId)进行关联

- comfyTaskId(prompt_id)

  即提交工作流返回的prompt_id

## 如何使用

- 添加Maven依赖

  ```xml
  <dependency>
      <groupId>io.github.sun-12138</groupId>
      <artifactId>comfyui-spring-boot-starter</artifactId>
      <version>1.0.3</version>
  </dependency>
  ```

- 添加配置

  - comfyui配置

    ```yaml
    spring:
      comfyui:
        # 主机地址 默认127.0.0.1
        host: 127.0.0.1
        # comfyui端口号 默认8188
        port: 8188
        # comfyui会话id 默认随机生成UUID
        client-id: test
        # 提交绘图任务时，如果Comfyui内部有非当前服务提交的任务时是否自动取消
        auto-clear-task: true
        queue:
          # 任务队列类型 目前支持: queue(阻塞队列)、rabbit 后续支持: rocket、kafka、custom
          type: queue
    ```

  - 若类型选择的消息队列则还需配置对应的消息队列配置

    1. RabbitMQ配置例子

       ```yaml
       spring:
         rabbitmq:
           host: 127.0.0.1
           port: 5672
           username: admin
           password: 123456
           virtual-host: /
           listener:
             simple:
               acknowledge-mode: manual # 手动ACK(一定要添加)
       ```

- 监听任务进度

  - 方法1: 实现ITaskProcessReceiver接口并注册为bean
  
    例:
  
    ```java
    @Slf4j
    @Component
    public class TaskProcessMonitor implements ITaskProcessReceiver {
    
        /**
         * 任务开始
         *
         * @param start 任务开始信息
         */
        @Override
        public void taskStart(ComfyTaskStart start) {
            log.info("任务开始, 任务id:{}", start.getTaskId());
        }
    
        /**
         * 当前执行的节点和节点执行进度
         *
         * @param progress 进度信息
         */
        @Override
        public void taskNodeProgress(ComfyTaskNodeProgress progress) {
            log.info("任务进度更新, 任务id: {}, 内部任务id: {}, 当前节点名: {}, 当前进度:{}%", progress.getTaskId(), progress.getComfyTaskId(), progress.getNode().getTitle(), progress.getPercent());
        }
    
        /**
         * 任务进度预览效果图
         *
         * @param preview 预览图信息
         */
        @Override
        public void taskProgressPreview(ComfyTaskProgressPreview preview) {
            log.info("任务进度预览, 任务id: {}, 内部任务id: {}, 预览: <图片>", preview.getTaskId(), preview.getComfyTaskId());
        }
    
        /**
         * 任务输出的图片
         *
         * @param output 输出信息
         */
        @Override
        public void taskOutput(ComfyTaskOutput output) {
            log.info("任务输出结果, 任务id: {}, 节点名: {}, 内部任务id: {}", output.getTaskId(), output.getNode().getTitle(), output.getComfyTaskId());
        }
    
        /**
         * 任务完成
         *
         * @param complete 任务完成信息
         */
        @Override
        public void taskComplete(ComfyTaskComplete complete) {
            log.info("任务完成, 任务id: {}, 内部任务id: {}", complete.getTaskId(), complete.getComfyTaskId());
        }
    
        /**
         * 任务失败
         *
         * @param error 任务错误信息
         */
        @Override
        public void taskError(ComfyTaskError error) {
            log.error("任务错误， 任务id:{}, 内部任务id: {}, 错误信息: {}", error.getTaskId(), error.getComfyTaskId(), error.getErrorInfo());
        }
    
        /**
         * 绘图队列任务个数更新
         *
         * @param taskNumber 队列任务信息
         */
        @Override
        public void taskNumberUpdate(ComfyTaskNumber taskNumber) {}
    
        /**
         * 当前系统负载
         *
         * @param performance 系统状态
         */
        @Override
        public void systemPerformance(ComfySystemPerformance performance) {}
    }
    ```

  - 方法2: 使用@TaskProcessListener注解
  
    例:
    
    ```java
    @TaskProcessListener(TaskProcessType.SYSTEM_PERFORMANCE)
    private void testListener(ComfySystemPerformance comfySystemPerformance) {
        log.info("系统性能监听器接收到消息：{}", comfySystemPerformance);
    }
    ```
    
  - 方法3: 发布订阅模式
  
    例:
  
    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class TestSubject {
  
    ```java
    /**
     * 任务进度发布者
     */
    private final TaskProcessSubjectPublisher publisher;
    
    /**
     * 资源文件加载者
     */
    private final ResourceLoader resourceLoader;
    
    /**
     * 任务提交者
     */
    private final IDrawingTaskSubmit taskSubmit;
    
    /**
     * SpringApplication启动完成后执行
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startSubscribe() {
        submitTask("test1");
        //订阅任务开始事件 可以订阅某个类型的事件而不是某个任务的事件
        publisher.subscribe(TaskProcessType.START, new TaskStartSubscriber(publisher));
        //订阅任务进度更新事件
        publisher.subscribe("test1", TaskProcessType.PROGRESS, new TaskProgressSubscriber());
        //订阅任务完成事件
        publisher.subscribe("test1", TaskProcessType.COMPLETE, new TaskCompleteSubscriber(publisher));
    }
    
    /**
     * 提交任务
     *
     * @param taskId 自定义任务id
     */
    public void submitTask(String taskId) {
        ComfyWorkFlow flow = getFlow();
        assert flow != null;
        ComfyWorkFlowNode node = flow.getNode("3");
        // 设置随机种子
        int randomSeed = Math.abs(new Random().nextInt());
        node.getInputs().put("seed", randomSeed);
        taskSubmit.submit(new DrawingTaskInfo(taskId, flow, 5, TimeUnit.MINUTES));
    }
    
    /**
     * 获取默认的工作流
     */
    private ComfyWorkFlow getFlow() {
        //从resources文件夹下读取default.json文件
        Resource resource = resourceLoader.getResource("classpath:" + "default.json");
        StringBuilder flowStr = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                flowStr.append(line);
            }
        } catch (IOException e) {
            return null;
        }
        return ComfyWorkFlow.of(flowStr.toString());
    }
    
    /**
     * 绘图任务开始订阅者
     */
    @RequiredArgsConstructor
    private static class TaskStartSubscriber implements ITaskProcessSubjectSubscriber {
    
        private final TaskProcessSubjectPublisher publisher;
    
        /**
         * 通知消息
         *
         * @param event 事件信息
         */
        @Override
        public <T extends IComfyTaskProcess> void notify(TaskProcessMessage<T> event) {
            ComfyTaskStart start = (ComfyTaskStart) event.getData();
            log.info("任务开始, 任务id:{}", start.getTaskId());
            //取消订阅
            publisher.unSubscribe(start.getTaskId(), TaskProcessType.START, this);
        }
    }
    
    /**
     * 绘图任务进度更新订阅者
     */
    private static class TaskProgressSubscriber implements ITaskProcessSubjectSubscriber {
    
        /**
         * 通知消息
         *
         * @param event 事件信息
         */
        @Override
        public <T extends IComfyTaskProcess> void notify(TaskProcessMessage<T> event) {
            ComfyTaskNodeProgress progress = (ComfyTaskNodeProgress) event.getData();
            log.info("任务进度更新, 任务id: {}, 内部任务id: {}, 当前节点名: {}, 当前进度:{}%", progress.getTaskId(), progress.getComfyTaskId(), progress.getNode().getTitle(), progress.getPercent());
        }
    }
    
    /**
     * 绘图任务完成订阅者
     */
    @RequiredArgsConstructor
    private static class TaskCompleteSubscriber implements ITaskProcessSubjectSubscriber {
    
        private final TaskProcessSubjectPublisher publisher;
    
        /**
         * 通知消息
         *
         * @param event 事件信息
         */
        @Override
        public <T extends IComfyTaskProcess> void notify(TaskProcessMessage<T> event) {
            ComfyTaskComplete complete = (ComfyTaskComplete) event.getData();
            log.info("任务完成, 任务id: {}, 内部任务id: {}", complete.getTaskId(), complete.getComfyTaskId());
            //取消订阅
            publisher.unSubscribe(complete.getTaskId(), TaskProcessType.PROGRESS, this);
        }
    }
    ```
- 提交任务

  获取IDrawingTaskSubmit类型的bean调用submit方法

  例:

  ```java
  @Slf4j
  @Component
  @RequiredArgsConstructor
  public class TestSubmit {
  
      private final IDrawingTaskSubmit taskSubmit;
      private final ResourceLoader resourceLoader;
  
      @PostConstruct
      public void testSubmitTask() {
          submitTask("test1");
          submitTask("test2");
          log.info("绘图任务提交完成");
      }
  
      /**
       * 提交任务
       *
       * @param taskId 自定义任务id
       */
      public void submitTask(String taskId) {
          ComfyWorkFlow flow = getFlow();
          assert flow != null;
          ComfyWorkFlowNode node = flow.getNode("3");
          // 设置随机种子
          int randomSeed = Math.abs(new Random().nextInt());
          node.inputs().put("seed", randomSeed);
          taskSubmit.submit(new DrawingTaskInfo(taskId, flow, 5, TimeUnit.MINUTES));
      }
  
      /**
       * 获取默认的工作流
       */
      private ComfyWorkFlow getFlow() {
          //从resources文件夹下读取default.json文件
          Resource resource = resourceLoader.getResource("classpath:" + "default.json");
          StringBuilder flowStr = new StringBuilder();
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
              String line;
              while ((line = reader.readLine()) != null) {
                  flowStr.append(line);
              }
          } catch (IOException e) {
              return null;
          }
          return ComfyWorkFlow.of(flowStr.toString());
      }
  }
  ```

## 修改工作流节点参数值

例:

```java
//工作流字符串
String workflowStr = "{...}";
ComfyWorkFlow flow = ComfyWorkFlow.of(workflowStr);
assert flow != null;
ComfyWorkFlowNode node = flow.getNode("3");
// 设置随机种子
int randomSeed = Math.abs(new Random().nextInt());
node.inputs().put("seed", randomSeed);
```

ComfyWorkFlowNode.inputes对应工作流节点中的inputs

```json
"3": {
  "inputs": {
    "seed": 156680208700286,
    "steps": 20,
    "cfg": 8,
    "sampler_name": "euler",
    "scheduler": "normal",
    "denoise": 1,
    "model": [
      "4",
      0
    ],
    "positive": [
      "6",
      0
    ],
    "negative": [
      "7",
      0
    ],
    "latent_image": [
      "5",
      0
    ]
  },
  "class_type": "KSampler",
  "_meta": {
    "title": "K采样器"
  }
}
```
