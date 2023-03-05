<img src="https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/readme/icon.png" align="right" />

[license-apache2.0]:https://www.apache.org/licenses/LICENSE-2.0.html
[github-action]:https://github.com/shiyindaxiaojie/eden-demo-layer/actions
[sonarcloud-dashboard]:https://sonarcloud.io/dashboard?id=shiyindaxiaojie_eden-demo-layer

# 分层架构

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/readme/language-java-blue.svg) [![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/readme/license-apache2.0-red.svg)][license-apache2.0] [![](https://github.com/shiyindaxiaojie/eden-demo-layer/workflows/build/badge.svg)][github-action] [![](https://sonarcloud.io/api/project_badges/measure?project=shiyindaxiaojie_eden-demo-layer&metric=alert_status)][sonarcloud-dashboard]

本项目使用分层架构构建，分层架构是《阿里巴巴Java开发手册》推荐使用的一种面向数据模型的架构风格，默认上层依赖于下层，例如 `Web` 层依赖 `Service` 层、`Service` 层又依赖 `DAO` 层，在垂直业务领域能够满足单一职责原则，通过 `Maven` 多模块化的开发模式，可以帮助降低复杂应用场景的系统熵值，提升系统开发和运维效率。

> 参考文档请查看 [WIKI](https://github.com/shiyindaxiaojie/eden-demo-layer/wiki) 。

## 组件构成

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/eden-demo-layer/component.png)

* **eden-demo-layer-api**：API层，对外以 `jar` 包的形式提供接口。
* **eden-demo-layer-dao**：数据持久层，与底层 `MySQL`、`Elasticsearch`、`MongoDB` 等进行数据交互。
* **eden-demo-layer-service**：业务逻辑服务层。
* **eden-demo-layer-manager**：通用业务处理层，对第三方平台进行接口封装，对 `Service` 层通用能力的下沉，如缓存方案、中间件通用处理，与 `DAO` 层交互，对多个 `DAO` 的组合复用。
* **eden-demo-layer-web**：请求处理层，对访问控制进行转发，各类基本参数校验，或者不复用的业务简单处理等。
* **eden-demo-layer-start**：程序启动入口，统一管理应用的配置和交付。

## 运行流程

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/eden-demo-layer/sequence.png)

## 如何构建

由于 `Spring Boot 2.4.x` 和 `Spring Boot 3.0.x` 在架构层面有很大的变更，因此笔者采取跟 Spring Boot 版本号一致的分支:

* 2.4.x 分支适用于 `Spring Boot 2.4.x`，最低支持 JDK 1.8。
* 2.7.x 分支适用于 `Spring Boot 2.7.x`，最低支持 JDK 11。
* 3.0.x 分支适用于 `Spring Boot 3.0.x`，最低支持 JDK 17。

本项目默认使用 Maven 来构建，最快的使用方式是 `git clone` 到本地。为了简化不必要的技术细节，本项目依赖 [eden-architect](https://github.com/shiyindaxiaojie/eden-architect)（孵化中），涉及的依赖项暂时没有发布到 Maven 中央仓库。请按下列步骤完成构建。

1. 克隆 [eden-architect](https://github.com/shiyindaxiaojie/eden-architect) 到本地，在根目录执行 `./mvnw install -T 4C` 安装到本地 Maven 仓库。
2. 在 `eden-demo-layer` 目录执行 `./mvnw install -T 4C` 完成本项目的构建。

## 如何启动

### 快速体验

本项目默认设置了 local 环境运行，所有外部的组件依赖均为关闭状态。

1. 在项目目录下运行 `mvn install`（如果不想运行测试，可以加上 `-DskipTests` 参数）。
2. 进入 `eden-demo-cola-start` 目录，执行 `mvn spring-boot:run` 或者启动 `ColaApplication` 类。运行成功的话，可以看到 `Spring Boot` 启动成功的界面。
3. 本应用中已经实现了一个简单的 `RestController` 接口，可以点击 [演示接口](http://localhost:8081/api/users/1) 进行调试。
4. 如果您加班累了，也可以点击 [http://localhost:8081](http://localhost:8081)，逃离地球 ☺

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/common/404.png)

### 微调配置

1. 修改 Nacos 客户端配置，您可以查阅 [Nacos Quick Start](https://nacos.io/zh-cn/docs/quick-start.html) 快速搭建，请根据您的 Nacos 地址修改配置文件：[bootstrap-local.yml](https://github.com/shiyindaxiaojie/eden-demo-cola/blob/main/eden-demo-cola-start/src/main/resources/config/bootstrap-local.yml)
2. 修改数据库的配置，本项目默认使用 H2 内存数据库启动，基于 `Liquibase` 在项目启动时自动初始化 SQL 脚本。如果您使用的是外部的 MySQL 数据库，可以从此处调整下数据库的连接信息：[application-local.yml](https://github.com/shiyindaxiaojie/eden-demo-cola/blob/main/eden-demo-cola-start/src/main/resources/config/application-local.yml)
3. 本项目罗列了 `Redis` 缓存、`RocketMQ` 消息队列、`ShardingSphere` 分库分表等常用组件的使用方案，默认通过 `xxx.enabled` 关闭自动配置。您可以根据实际情况开启配置，直接完成组件的集成。

## 版本规范

项目的版本号格式为 `x.y.z` 的形式，其中 x 的数值类型为数字，从 0 开始取值，且不限于 0~9 这个范围。项目处于孵化器阶段时，第一位版本号固定使用 0，即版本号为 `0.x.x` 的格式。

* 孵化版本：0.0.1-SNAPSHOT
* 开发版本：1.0.0-SNAPSHOT
* 发布版本：1.0.0

版本迭代规则：

* 1.0.0 <> 1.0.1：兼容
* 1.0.0 <> 1.1.0：基本兼容
* 1.0.0 <> 2.0.0：不兼容

## 持续集成

CI/CD 工具选型：Jenkins、Zadig、Codeup、CODING...

### CODING 持续交付

下图演示基于 CODING 实现持续构建、持续部署的效果

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/common/coding-cicd.png)

测试报告可视化

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/common/coding-test-report.png)

## 最佳实践

### 经典分层架构设计

> TODO

### Git 多人协作分支管理

在敏捷开发盛行的时代，`GitFlow` 显得力不从心，笔者为团队制定了一套简单易用的流程。

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/processon/git-action.png)

### CAT 可观测性方案

通过 `TraceId` 分析整个链路的 `HTTP` 请求耗时、`RPC` 调用情况、`Log` 业务日志、`SQL` 和 `Cache` 执行耗时。[传送门](https://github.com/shiyindaxiaojie/cat)

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/cat/tracing.png)

### Sentinel 流量治理方案

根据业务负载配置您的流控规则，并允许在任意时刻查看监控数据。[传送门](https://github.com/shiyindaxiaojie/Sentinel)

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/sentinel/sentinel-dashboard-overview.png)

### Arthas 在线诊断工具

无需在每个应用部署探针，自动发现服务，开箱即用，允许在低负载环境诊断你的应用。[传送门](https://github.com/shiyindaxiaojie/arthas)

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/arthas/arthas-dashboard-overview.png)
