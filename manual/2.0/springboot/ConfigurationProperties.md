# buession-springboot-cli 参考手册


## 配置属性


#### 通用配置

|  属性   | 类型   | 默认值    | 说明    |
|  ----  | ----   | ----     | ----  |
| spring.canal.thread.name-prefix                    | String                                   | canal-execute      | 线程名称前缀          |
| spring.canal.thread.core-pool-size                 | Integer                                  | -                  | 线程池核心线程大小     |
| spring.canal.thread.maximum-pool-size              | Integer                                  | 0                  | 空闲线程存活时间       |
| spring.canal.thread.policy                         | Integer                                  | DISCARD            | 饱和策略              |


#### Kafka 适配器模式

|  属性   | 类型   | 默认值    | 说明    |
|  ----  | ----   | ----     | ----  |
| spring.canal.kafka.servers                    		| String                                   | -      | Kafka 主机地址，多个以";"分割          |
| spring.canal.kafka.instances.[key].group-id           | String                                   | -      | Group Id                            |
| spring.canal.kafka.instances.[key].partition          | Integer                                  | -      | 分区                                 |
| spring.canal.kafka.instances.[key].destination        | String                                   | -      | 指令                                 |
| spring.canal.kafka.instances.[key].filter             | String                                   | -      | 过滤规则                              |
| spring.canal.kafka.instances.[key].batch-size         | Integer                                  | 1      | 批处理条数                            |
| spring.canal.kafka.instances.[key].timeout            | java.time.Duration                       | 10 s   | 超时时长                              |
| spring.canal.kafka.instances.[key].flat-message       | boolean                                  | false  |                                      |


#### PulsarMQ 适配器模式

|  属性   | 类型   | 默认值    | 说明    |
|  ----  | ----   | ----     | ----  |
| spring.canal.pulsar.service-url                    	 | String                                   | -      | PulsarMQ 服务地址，多个以";"分割       |
| spring.canal.pulsar.role-token                    	 | String                                   | -      | Role Token                          |
| spring.canal.pulsar.get-batch-timeout                  | Integer                                  | 30     |                                     |
| spring.canal.pulsar.batch-process-timeout              | Integer                                  | 60     |                                     |
| spring.canal.pulsar.redelivery-delay                   | Integer                                  | 60     |                                     |
| spring.canal.pulsar.ack-timeout                   	 | Integer                                  | 30     |                                     |
| spring.canal.pulsar.retry                       		 | boolean                                  | true   | 是否重试                             |
| spring.canal.pulsar.retryDLQUpperCase                  | boolean                                  | true   |                                     |
| spring.canal.pulsar.max-redelivery-count               | Integer                                  | -      |                                     |
| spring.canal.pulsar.instances.[key].subscript-name     | String                                   | -      | 订阅名称                             |
| spring.canal.pulsar.instances.[key].destination        | String                                   | -      | 指令                                 |
| spring.canal.pulsar.instances.[key].filter             | String                                   | -      | 过滤规则                              |
| spring.canal.pulsar.instances.[key].batch-size         | Integer                                  | 1      | 批处理条数                            |
| spring.canal.pulsar.instances.[key].timeout            | java.time.Duration                       | 10 s   | 超时时长                              |
| spring.canal.pulsar.instances.[key].flat-message       | boolean                                  | false  |                                      |


#### RabbitMQ 适配器模式

|  属性   | 类型   | 默认值    | 说明    |
|  ----  | ----   | ----     | ----  |
| spring.canal.rabbit.server                    	     | String                                   | -      | RabbitMQ 主机地址                    |
| spring.canal.rabbit.virtual-host                       | String                                   | -      | Virtual Host                        |
| spring.canal.rabbit.username                    		 | String                                   | -      | 用户名                               |
| spring.canal.rabbit.password                    		 | String                                   | -      | 密码                                 |
| spring.canal.rabbit.instances.[key].destination        | String                                   | -      | 指令                                 |
| spring.canal.rabbit.instances.[key].filter             | String                                   | -      | 过滤规则                              |
| spring.canal.rabbit.instances.[key].batch-size         | Integer                                  | 1      | 批处理条数                            |
| spring.canal.rabbit.instances.[key].timeout            | java.time.Duration                       | 10 s   | 超时时长                              |
| spring.canal.rabbit.instances.[key].flat-message       | boolean                                  | false  |                                      |


#### RocketMQ 适配器模式

|  属性   | 类型   | 默认值    | 说明    |
|  ----  | ----   | ----     | ----  |
| spring.canal.rocket.name-server                    	 | String                                   | -      | RocketMQ NameServer 地址            |
| spring.canal.rocket.enable-message-trace               | Boolean                                  | -      | 是否启用消息跟踪                      |
| spring.canal.rocket.customized-trace-topic             | String                                   | -      |                                     |
| spring.canal.rocket.access-channel                     | String                                   | -      | RocketMQ NameServer 地址             |
| spring.canal.rocket.instances.[key].group-id           | String                                   | -      | Group Id                            |
| spring.canal.rocket.instances.[key].namespace          | String                                   | -      | 名称空间                              |
| spring.canal.rocket.instances.[key].destination        | String                                   | -      | 指令                                 |
| spring.canal.rocket.instances.[key].filter             | String                                   | -      | 过滤规则                              |
| spring.canal.rocket.instances.[key].batch-size         | Integer                                  | 1      | 批处理条数                            |
| spring.canal.rocket.instances.[key].timeout            | java.time.Duration                       | 10 s   | 超时时长                              |
| spring.canal.rocket.instances.[key].flat-message       | boolean                                  | false  |                                      |


#### RocketMQ 适配器模式

|  属性   | 类型   | 默认值    | 说明    |
|  ----  | ----   | ----     | ----  |
| spring.canal.tcp.server                    	      | String                                   | -      | 主机地址                              |
| spring.canal.tcp.zk-servers                    	  | String                                   | -      | Zookeeper 主机地址                   |
| spring.canal.tcp.username                           | String                                   | -      | 用户名                               |
| spring.canal.tcp.password                           | String                                   | -      | 密码                                 |
| spring.canal.tcp.instances.[key].group-id           | String                                   | -      | Group Id                            |
| spring.canal.tcp.instances.[key].namespace          | String                                   | -      | 名称空间                              |
| spring.canal.tcp.instances.[key].destination        | String                                   | -      | 指令                                 |
| spring.canal.tcp.instances.[key].filter             | String                                   | -      | 过滤规则                              |
| spring.canal.tcp.instances.[key].batch-size         | Integer                                  | 1      | 批处理条数                            |
| spring.canal.tcp.instances.[key].timeout            | java.time.Duration                       | 10 s   | 超时时长                              |
| spring.canal.tcp.instances.[key].flat-message       | boolean                                  | false  |                                      |