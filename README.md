<img src="https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/readme/icon.png" align="right" />

[license-apache2.0]:https://www.apache.org/licenses/LICENSE-2.0.html
[github-action]:https://github.com/shiyindaxiaojie/eden-demo-layer/actions
[sonarcloud-dashboard]:https://sonarcloud.io/dashboard?id=shiyindaxiaojie_eden-demo-layer

# 分层架构

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/readme/language-java-blue.svg) [![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/readme/license-apache2.0-red.svg)][license-apache2.0] [![](https://github.com/shiyindaxiaojie/eden-demo-layer/actions/workflows/maven-ci.yml/badge.svg?branch=main)][github-action] [![](https://sonarcloud.io/api/project_badges/measure?project=shiyindaxiaojie_eden-demo-layer&metric=alert_status)][sonarcloud-dashboard]

本项目使用分层架构构建，分层架构是《阿里巴巴Java开发手册》推荐使用的一种面向数据模型的架构风格，默认上层依赖于下层，例如 `Web` 层依赖 `Service` 层、`Service` 层又依赖 `DAO` 层，在垂直业务领域能够满足单一职责原则，通过 `Maven` 多模块化的开发模式，可以帮助降低复杂应用场景的系统熵值，提升系统开发和运维效率。

> 参考文档请查看 [WIKI](https://github.com/shiyindaxiaojie/eden-demo-layer/wiki) 。

## 组件构成

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/eden-demo-layer/component.png)

* **eden-demo-layer-api**：API层，对外以 `jar` 包的形式提供接口。
* **eden-demo-layer-dao**：数据持久层，与底层 `MySQL`、`Elasticsearch`、`MongoDB` 等进行数据交互。
* **eden-demo-layer-service**：业务逻辑服务层。
* **eden-demo-layer-manager**：通用业务处理层，对第三方平台进行接口封装，对 `Service` 层通用能力的下沉，如缓存方案、中间件通用处理，与 `DAO` 层交互，对多个 `DAO` 的组合复用。
* **eden-demo-layer-web**：请求处理层，对访问控制进行转发，各类基本参数校验，或者不复用的业务简单处理等。
* **eden-demo-layer-start**：程序启动入口，统一管理应用的配置和交付。

## 运行流程

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/eden-demo-layer/sequence.png)

## 如何构建

由于 `Spring Boot 2.4.x` 和 `Spring Boot 3.0.x` 在架构层面有很大的变更，因此笔者采取跟 Spring Boot 版本号一致的分支:

* 2.4.x 分支适用于 `Spring Boot 2.4.x`，最低支持 JDK 1.8。
* 2.7.x 分支适用于 `Spring Boot 2.7.x`，最低支持 JDK 11。
* 3.0.x 分支适用于 `Spring Boot 3.0.x`，最低支持 JDK 17。

本项目默认使用 Maven 来构建，最快的使用方式是 `git clone` 到本地。为了简化不必要的技术细节，本项目依赖 [eden-architect](https://github.com/shiyindaxiaojie/eden-architect)，在项目的根目录执行 `mvn install -T 4C` 完成本项目的构建。

## 如何启动

### 快速体验

本项目默认设置了 local 环境运行，所有外部的组件依赖均为关闭状态。

1. 在项目目录下运行 `mvn install`（如果不想运行测试，可以加上 `-DskipTests` 参数）。
2. 进入 `eden-demo-layer-start` 目录，执行 `mvn spring-boot:run` 或者启动 `LayerApplication` 类。运行成功的话，可以看到 `Spring Boot` 启动成功的界面。
3. 本应用中已经实现了一个简单的 `RestController` 接口，可以点击 [演示接口](http://localhost:8081/api/users/1) 进行调试。
4. 由于目前的主流是前后端分离开发，请按需实现页面。访问 [http://localhost:8082](http://localhost:8082) 将跳转到 404 页面。

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/common/404.png)

### 微调配置

**开启配置中心**：推荐使用 `Nacos` 组件，您可以查阅 [Nacos Quick Start](https://nacos.io/zh-cn/docs/quick-start.html) 快速搭建，请根据您的 Nacos 地址修改配置文件：[bootstrap-dev.yml](https://github.com/shiyindaxiaojie/eden-demo-layer/blob/main/eden-demo-layer-start/src/main/resources/config/bootstrap-dev.yml)，调整以下内容：

```yaml
spring:
    cloud:
        nacos:
            discovery: # 注册中心
                enabled: true # 默认关闭，请按需开启
            config: # 配置中心
                enabled: true # 默认关闭，请按需开启
```

**修改默认的数据源**：本项目默认使用 `H2` 内存数据库启动，基于 `Liquibase` 在项目启动时自动初始化 SQL 脚本。如果您使用的是外部的 MySQL 数据库，可以从此处调整下数据库的连接信息：[application-local.yml](https://github.com/shiyindaxiaojie/eden-demo-layer/blob/main/eden-demo-layer-start/src/main/resources/config/application-local.yml)，请删除任何与 `H2` 有关的配置。

```yaml
spring:
    #  h2: # 内存数据库
    #    console:
    #      enabled: true # 线上环境请勿设置
    #      path: /h2-console
    #      settings:
    #        trace: false
    #        web-allow-others: false
    datasource: # 数据源管理
        username:
        password:
        url: jdbc:mysql://host:port/schema?rewriteBatchedStatements=true&useSSL=false&useOldAliasMetadataBehavior=true&useUnicode=true&serverTimezone=GMT%2B8
        driver-class-name: com.mysql.cj.jdbc.Driver
```

此外，本项目还罗列了 `Redis` 缓存、`RocketMQ` 消息队列、`ShardingSphere` 分库分表等常用组件的使用方案，默认通过 `xxx.enabled` 关闭自动配置。您可以根据实际情况开启配置，直接完成组件的集成。

## 如何部署

### JAR 程序部署

在项目根目录执行以下命令，生成 JAR 可执行程序。

```bash
mvn -T 4C -U package
mvn -T 4C deploy # 可选项，发布依赖到私服
```

### Jib 镜像部署

Google Jib 插件允许您在没有安装 Docker 下完成镜像的构建。

```bash
mvn -T 4C -U package
mvn -pl eden-demo-layer-start jib:build -Djib.disableUpdateChecks=true -DskipTests
```

### Docker 容器部署

本项目使用了 Spring Boot 的镜像分层特性优化了镜像的构建效率，请确保正确安装了 Docker 工具，然后执行以下命令。

```bash
docker build -f docker/Dockerfile -t eden-demo-layer:{tag} .
```

### Helm 打包部署

以应用为中心，建议使用 Helm 统一管理所需部署的 K8s 资源描述文件，请参考以下命令完成应用的安装和卸载。

```bash
helm install eden-demo-layer ./helm # 部署资源
helm uninstall eden-demo-layer # 卸载资源
```

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

> CI/CD 工具选型：Jenkins、Zadig、Codeup、CODING

### CODING 持续交付

下图演示基于 CODING 实现持续构建、持续部署的效果。[传送门](https://www.yuque.com/mengxiangge/action/coding)

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/common/coding-cicd.png)

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/common/coding-test-report.png)

## 最佳实践

### 经典分层架构设计

> TODO, Coming soon

### Git 多人协作分支管理

在敏捷开发盛行的时代，`GitFlow` 显得力不从心，笔者为团队制定了一套简单易用的流程。

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/processon/git-action.png)

### CAT 可观测性方案

通过 `TraceId` 分析整个链路的 `HTTP` 请求耗时、`RPC` 调用情况、`Log` 业务日志、`SQL` 和 `Cache` 执行耗时。[传送门](https://github.com/shiyindaxiaojie/cat)

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/cat/tracing.png)

### Sentinel 流量治理方案

根据业务负载配置您的流控规则，并允许在任意时刻查看接口的 QPS 和限流情况。[传送门](https://github.com/shiyindaxiaojie/Sentinel)

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/sentinel/sentinel-dashboard-overview-custom.png)

### Arthas 在线诊断工具

使用动态时运行探针，自动发现服务，开箱即用，允许在低负载环境诊断你的应用。[传送门](https://github.com/shiyindaxiaojie/arthas)

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/images/arthas/arthas-dashboard-overview.png)

## 变更日志

请查阅 [CHANGELOG.md](https://github.com/shiyindaxiaojie/eden-demo-layer/blob/main/CHANGELOG.md)
