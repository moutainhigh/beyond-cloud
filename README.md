## **`项目简介`**
`spring cloud` 是一个学习 `spring cloud` 的演示项目, 并集成一些分布式中间件

## 各 Module 介绍

| Module 名称                                                 | Module 介绍                           |
| ---------------------------------------------------------- | ------------------------------------ |
| [beyond-cloud-common](./beyond-cloud-common)               | 公共模块                               |
| [beyond-cloud-framework](./beyond-cloud-framework)         | 中间件配置模块                          |
| [beyond-cloud-svc-account](./beyond-cloud-svc-account)     | 账户模块                               |
| [beyond-cloud-svc-business](./beyond-cloud-svc-business)   | 业务模块                               |
| [beyond-cloud-svc-order](./beyond-cloud-svc-order)         | 订单模块                               |
| [beyond-cloud-svc-storage](./beyond-cloud-svc-storage)     | 库存模块                               |

## 中间件
### 1. 分布式事物管理 -- Seata
* Seata 是一款开源的分布式事务解决方案, 致力于在微服务架构下提供高性能和简单易用的分布式事务服务. 此项目采用 consul 为注册中心, file 为配置中心.
* 关键配置

| 配置项                                       | 参数值              | 备注                |
| --------------------------------------------| -------------------|--------------------|
| spring.cloud.alibaba.seata.tx-service-group | beyond_tx_group    | 事物组名称           |
| seata.registry.type                         | consul             | 注册中心类型         |
| seata.registry.consul.cluster               | seata-server       | 中间件配置模块        |
| seata.config.type                           | file               | 账户模块             |
| seata.service.vgroup-mapping--key           | beyond_tx_group    | 与事物组名称一致       | 
| seata.service.vgroup-mapping--value         | seata-server       | 与注册中心类型一致     |

* [详细集成配置](./readme/README-SEATA.md)
