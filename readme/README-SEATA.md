# SpringCloud + OpenFeign 集成 Seata
## 1. 前置条件
* 基于支持本地 ACID 事务的关系型数据库。
* Java 应用，通过 JDBC 访问数据库。
* 每个微服务已完成对 MySQL + Druid + Mybatis 的集成

## 2. 安装 seata server
* 请参考 [CentOS 7 安装 Seata Server](https://github.com/beyond0630/notes/blob/master/seata/install.md)

## 3. 集成步骤
### 1) 导入包
```pom
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-seata</artifactId>
    <version>2.2.0.RELEASE</version>
    <exclusions>
        <exclusion>
            <groupId>io.seata</groupId>
            <artifactId>seata-spring-boot-starter</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

### 2) application.yml 添加对 seata 配置
```yaml
spring:
  cloud:
    alibaba:
      seata:
        tx-service-group: beyond_tx_group          # 事物组

seata:
  enabled: true
  enable-auto-data-source-proxy: true              # 开启数据库自动代理
  registry:
    type: consul
    consul:
      cluster: seata-server                        # seata 注册到 consul 的集群服务名
      server-addr: ${CONSUL_HOST:10.0.0.121}:8500
  config:
    type: file
    file:
      name: file.conf
  transport:
    type: TCP
    server: NIO
    heartbeat: true
    enable-client-batch-send-request: true          # 客户端事务消息请求是否批量合并发送（默认true）
    thread-factory:
      boss-thread-prefix: NettyBoss
      worker-thread-prefix: NettyServerNIOWorker
      server-executor-thread-prefix: NettyServerBizHandler
      share-boss-worker: true
      client-selector-thread-prefix: NettyClientSelector
      client-selector-thread-size: 1
      client-worker-thread-prefix: NettyClientWorkerThread
      boss-thread-size: 1
      worker-thread-size: default
    shutdown:
      wait: 3
    compressor: none
    serialization: seata
  service:
    vgroup-mapping:
      beyond_tx_group: seata-server                 # TC 集群（必须与 seata-server 保持一致）
    grouplist:
      seata-server: 127.0.0.1:8091
    enable-degrade: false                           # 降级开关
    disable-global-transaction: false               # 禁用全局事务（默认false）
  client:
    rm:
      async-commit-buffer-limit: 10000              # 异步提交缓存队列长度（默认10000）
      report-retry-count: 5                         # 一阶段结果上报TC重试次数（默认5）
      table-meta-check-enable: false                # 自动刷新缓存中的表结构（默认false）
      report-success-enable: false
      lock:
        retry-interval: 10                          # 校验或占用全局锁重试间隔（默认10ms）
        retry-times: 30                             # 校验或占用全局锁重试次数（默认30）
        retry-policy-branch-rollback-on-conflict: true # 分支事务与其它全局回滚事务冲突时锁策略（优先释放本地锁让回滚成功）
    tm:
      commit-retry-count: 3                         # 一阶段全局提交结果上报TC重试次数（默认1次，建议大于1）
      rollback-retry-count: 3                       # 一阶段全局回滚结果上报TC重试次数（默认1次，建议大于1）
    undo:
      data-validation: true                         # 二阶段回滚镜像校验（默认true开启）
      log-serialization: jackson                    # undo序列化方式（默认jackson）
      log-table: undo_log                           # 自定义undo表名（默认undo_log）
    log:
      exception-rate: 100                           # 日志异常输出概率（默认100）


```

### 3) 数据源代理
* 这个是要特别注意的地方, seata对数据源做了代理和接管, 在每个参与分布式事务的服务中, 都要做如下配置.

```java
/**
 * 数据源代理
 *
 * @author lucifer
 * @date 2020/7/29 16:57
 */
@Configuration
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
public class DataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath*:/com/beyond/svc/**/mapper/**/*.xml"));
        SqlSessionFactory factory;
        try {
            factory = bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return factory;
    }
}
```
* 为适应 DruidDataSource 能正常实例, 数据源修改为如下配置
```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/beyond-cloud-svc-storage?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&useSSL=false
      username: root
      password: lucifer
      initial-size: 5
      max-active: 20
```

### 4) 开启分布式事物
* 在有通过 Feign 远程微服务调用且涉及事物的业务层方法加 @GlobalTransactional
```java
@Override
@GlobalTransactional(name = "purchase", rollbackFor = Exception.class)
public ApiResult purchase(final int userId, final String commodityCode, final int orderCount) {
    ApiResult<Boolean> deductResult = storageClient.deduct(commodityCode, orderCount);
    ApiResult<Order> orderResult = orderClient.createOrder(userId, commodityCode, orderCount);
    return ApiResult.ok();
}
```
