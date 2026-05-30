# Enterprise Human Resource Management Platform

企业人事管理后端服务，提供员工与组织架构管理、考勤签到、请假审批、公告发布、操作日志与仪表盘统计等能力。

## 技术栈

| 技术 | 版本 / 说明 |
|------|-------------|
| Java | 21 |
| Spring Boot | 3.2.0 |
| MyBatis | 3.0.3 |
| MySQL | 8.x |
| Redis | Token 会话校验 |
| JWT | 登录鉴权 |
| SpringDoc OpenAPI | 接口文档 |
| 阿里云 OSS | 员工头像上传 |
| PageHelper | 分页查询 |
| AOP | `@OperateLog` 操作日志 |

## 功能模块

| 模块 | 路径前缀 | 说明 |
|------|----------|------|
| 用户 | `/user` | 注册、登录、登出、个人信息、修改密码 |
| 员工 | `/employee` | 员工 CRUD、分页查询、头像上传 |
| 部门 | `/dept` | 部门 CRUD、列表 |
| 职位 | `/position` | 职位 CRUD、按部门查询 |
| 考勤 | `/attendance` | 签到 / 签退、个人记录、管理员分页与修正 |
| 请假 | `/leave` | 申请、我的记录、审批、分页查询 |
| 公告 | `/notice` | 发布、分页、详情、删除 |
| 日志 | `/log` | 操作日志分页（管理员） |
| 仪表盘 | `/dashborad` | 首页统计、本月考勤、部门人数、本月请假 |

> 说明：仪表盘路径为项目内历史命名 `dashborad`（非 dashboard）。

## 环境要求

- JDK 21+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- （可选）阿里云 OSS，用于头像存储

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd Enterprise-Human-Resource-Management-Platform
```

### 2. 初始化数据库

创建数据库并导入脚本：

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS emp_sys DEFAULT CHARSET utf8mb4;"
mysql -u root -p emp_sys < src/main/resources/emp_sys.sql
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
server:
  port: 8084

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/emp_sys?...
    username: root
    password: 你的密码
  data:
    redis:
      host: localhost
      port: 6379

aliyun:
  oss:
    endpoint: https://oss-cn-beijing.aliyuncs.com
    access-key-id: 你的 AccessKeyId
    access-key-secret: 你的 AccessKeySecret
    bucket-name: 你的 Bucket
    url-prefix: https://你的域名/
```

请勿将真实密钥提交到 Git，生产环境建议使用环境变量或配置中心。

### 4. 启动 Redis

```bash
redis-server
```

### 5. 运行项目

```bash
mvn spring-boot:run
```

或在 IDEA 中运行主类 `com.company.management.ManagementApplication`。

启动成功后默认端口：**8084**。

### 6. 接口文档

浏览器访问：

- Swagger UI：<http://localhost:8084/swagger-ui/index.html>
- OpenAPI JSON：<http://localhost:8084/v3/api-docs>

## 认证说明

除以下接口外，其余请求均需在 Header 中携带 Token：

```
Authorization: <登录返回的 token>
```

**无需登录的接口：**

- `POST /user/login`
- `POST /user/register`

登录后 Token 会写入 Redis，拦截器 `LoginInterceptor` 负责校验；当前用户信息通过 `ThreadLocal` 传递。

**角色说明：**

- `role = 0`：管理员（可访问日志、仪表盘等管理接口）
- 其他角色：普通员工

## 仪表盘接口

管理员专用（`role = 0`）：

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/dashborad` | 聚合数据（推荐前端一次拉取） |
| GET | `/dashborad/static` | 概览：员工数、部门数、昨日正常考勤、待审批请假 |
| GET | `/dashborad/month-attendance` | 本月考勤统计、按日统计、明细列表 |
| GET | `/dashborad/dept-employee-count` | 各部门员工人数 |
| GET | `/dashborad/month-leave` | 本月请假统计、类型分布、记录列表 |

## 操作日志

通过 `@OperateLog` 注解 + AOP 切面自动写入 `sys_log` 表，记录操作类型、请求方法、参数（密码字段脱敏）、IP、用户 ID 等。

仅标注了该注解的 Controller 方法会记日志。

## 项目结构

```
src/main/java/com/company/management/
├── annotation/      # 自定义注解（OperateLog）
├── aspect/          # AOP 切面
├── config/          # 配置类（Web、OSS）
├── controller/      # 接口层
├── dto/             # 请求参数对象
├── entity/          # 实体类
├── exception/       # 全局异常处理
├── interceptors/    # 登录拦截器
├── mapper/          # MyBatis Mapper
├── service/         # 业务接口与实现
├── task/            # 定时任务（考勤）
├── utils/           # 工具类（JWT、Redis、OSS 等）
└── vo/              # 视图对象

src/main/resources/
├── application.yml  # 应用配置
├── emp_sys.sql      # 数据库脚本
└── mapper/          # MyBatis XML
```

## 考勤状态说明

| status | 含义 |
|--------|------|
| 0 | 正常 |
| 1 | 迟到 |
| 2 | 早退 |
| 3 | 缺卡 |
| 4 | 请假 |

## 请假审批状态

| status | 含义 |
|--------|------|
| 0 | 待审批 |
| 1 | 已通过 |
| 2 | 已拒绝 |

## 构建与打包

```bash
mvn clean package -DskipTests
```

生成的 jar 位于 `target/management-0.0.1-SNAPSHOT.jar`。

## 常见问题

**1. 启动报数据库连接失败**  
检查 MySQL 是否启动、`emp_sys` 是否已导入、`application.yml` 账号密码是否正确。

**2. 登录后其他接口返回 401**  
确认 Redis 已启动；请求头是否携带 `Authorization`；Token 是否与 Redis 中一致。

**3. 操作日志 `params` 为空**  
需使用 `@Param("log")` 映射插入（项目已处理）；重新编译后再试。

**4. 本机 IP 显示为 127.0.0.1**  
本地访问时正常；生产环境经 Nginx 转发时需配置 `X-Forwarded-For` / `X-Real-IP`。

## 许可证

本项目仅供学习与交流使用。
