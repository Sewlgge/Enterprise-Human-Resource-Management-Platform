# 企业人事管理平台

前后端分离的企业人事管理系统，涵盖员工与组织架构、考勤签到、请假审批、公告通知、操作日志与数据仪表盘等功能。

| 模块 | 说明 |
|------|------|
| 后端 | Spring Boot 3 + MyBatis，默认端口 `8084` |
| 前端 | Vue 3 + Element Plus + Vite，开发端口 `5173` |
| 数据库 | MySQL 8，库名 `emp_sys` |

## 功能概览

### 管理员（`role = 0`）

- **仪表盘**：员工/部门概览、考勤与请假统计（ECharts 图表）
- **员工管理**：员工档案 CRUD、头像上传
- **部门 / 职位管理**：组织架构维护
- **考勤管理**：按日期分页查询（默认今天），支持修正签到记录
- **请假审批**：审批员工请假申请
- **操作日志**：查看系统操作记录
- **公告通知**：发布、草稿、编辑、删除

### 普通员工

- **我的考勤**：签到 / 签退、个人考勤记录
- **我的请假**：提交请假、查看审批结果
- **公告通知**：浏览已发布公告
- **个人中心**：修改资料、上传头像、修改密码

### 公共能力

- 用户注册（创建员工档案并开通账号）
- JWT + Redis 登录态校验
- 阿里云 OSS 头像存储（可配置）
- `@OperateLog` 自动记录操作日志

## 技术栈

### 后端

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
| AOP | 操作日志切面 |

### 前端

| 技术 | 版本 / 说明 |
|------|-------------|
| Vue | 3.5 |
| Vue Router | 5.x |
| Pinia | 3.x |
| Element Plus | 2.x |
| ECharts | 6.x |
| Vite | 8.x |

## 项目结构

```
management/
├── src/main/java/com/company/management/   # 后端源码
│   ├── controller/                         # REST 接口
│   ├── service/                            # 业务逻辑
│   ├── mapper/                             # MyBatis Mapper
│   ├── dto/ / vo/ / entity/                # 数据对象
│   ├── interceptors/                       # 登录拦截器
│   ├── aspect/                             # 操作日志 AOP
│   ├── task/                               # 考勤定时任务
│   └── utils/                              # JWT、Redis、OSS 等
├── src/main/resources/
│   ├── application.yml                     # 应用配置
│   ├── emp_sys.sql                         # 数据库初始化脚本
│   └── mapper/                             # MyBatis XML
├── Frontend/                               # 前端工程
│   ├── src/
│   │   ├── views/                          # 页面组件
│   │   ├── layouts/                        # 布局
│   │   ├── router/                         # 路由
│   │   ├── stores/                         # Pinia 状态
│   │   └── services/api.js                 # API 封装
│   └── vite.config.js                      # 开发代理 /api → 8084
└── pom.xml
```

## 环境要求

- **JDK** 21+
- **Maven** 3.6+
- **Node.js** 20.19+ 或 22.12+
- **MySQL** 8.0+
- **Redis** 6.0+
- （可选）阿里云 OSS，用于头像存储

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd management
```

### 2. 初始化数据库

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS emp_sys DEFAULT CHARSET utf8mb4;"
mysql -u root -p emp_sys < src/main/resources/emp_sys.sql
```

### 3. 修改后端配置

编辑 `src/main/resources/application.yml`：

```yaml
server:
  port: 8084

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/emp_sys?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 你的密码
  data:
    redis:
      host: localhost
      port: 6379

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.company.management.entity
  configuration:
    map-underscore-to-camel-case: true

aliyun:
  oss:
    endpoint: https://oss-cn-beijing.aliyuncs.com
    access-key-id: 你的 AccessKeyId
    access-key-secret: 你的 AccessKeySecret
    bucket-name: 你的 Bucket
    url-prefix: https://你的域名/
```

> 请勿将真实密钥提交到 Git。生产环境建议使用环境变量或配置中心。

### 4. 启动 Redis

```bash
redis-server
```

### 5. 启动后端

```bash
mvn spring-boot:run
```

或在 IDEA 中运行主类 `com.company.management.ManagementApplication`。

启动成功后访问：<http://localhost:8084>

### 6. 启动前端

```bash
cd Frontend
npm install
npm run dev
```

浏览器访问：<http://localhost:5173>

前端通过 Vite 代理将 `/api` 转发到后端 `http://localhost:8084`，无需额外配置 CORS。

### 7. 接口文档

- Swagger UI：<http://localhost:8084/swagger-ui/index.html>
- OpenAPI JSON：<http://localhost:8084/v3/api-docs>

## 默认账号

导入 `emp_sys.sql` 后可使用以下测试账号（密码均为 **123456**）：

| 用户名 | 角色 | 说明 |
|--------|------|------|
| `admin` | 管理员 | 可访问全部管理功能 |
| `zhangsan` | 员工 | 普通员工示例 |
| `lisi` | 员工 | 普通员工示例 |

也可通过注册页自助注册，系统会同时创建用户账号与员工档案。

## 认证说明

除以下接口外，其余请求均需在 Header 中携带 Token：

```
Authorization: <登录返回的 token>
```

**无需登录的接口：**

- `POST /user/login`
- `POST /user/register`
- `GET /user/register/depts`
- `GET /user/register/positions`

登录后 Token 写入 Redis，由 `LoginInterceptor` 校验；当前用户信息通过 `ThreadLocal` 传递。

**角色说明：**

| role | 含义 |
|------|------|
| `0` | 管理员 |
| `1` | 普通员工 |

## 主要 API 模块

| 模块 | 路径前缀 | 说明 |
|------|----------|------|
| 用户 | `/user` | 注册、登录、登出、个人信息、改密 |
| 员工 | `/employee` | 员工 CRUD、分页、头像上传 |
| 部门 | `/dept` | 部门 CRUD、列表 |
| 职位 | `/position` | 职位 CRUD、按部门查询 |
| 考勤 | `/attendance` | 签到/签退、个人记录、按日期分页与修正 |
| 请假 | `/leave` | 申请、我的记录、审批、分页 |
| 公告 | `/notice` | 发布、草稿、分页、详情、删除 |
| 日志 | `/log` | 操作日志分页（管理员） |
| 仪表盘 | `/dashborad` | 首页统计（历史命名，非 dashboard） |

### 考勤管理查询示例

管理员分页接口 `GET /attendance/page` 支持按日期查询：

```
GET /attendance/page?page=1&size=10&attendanceDate=2026-05-31&status=0
```

未传 `attendanceDate` 时，后端默认查询当天记录。

## 业务状态码

### 考勤状态

| status | 含义 |
|--------|------|
| 0 | 正常 |
| 1 | 迟到 |
| 2 | 早退 |
| 3 | 缺卡 |
| 4 | 请假 |

### 请假审批状态

| status | 含义 |
|--------|------|
| 0 | 待审批 |
| 1 | 已通过 |
| 2 | 已拒绝 |

## 定时任务

`AttendanceTask` 会在以下时机执行：

- **服务启动时**：初始化当天考勤记录、更新前一天缺卡状态
- **每日 0 点**：同上，为所有在职员工生成当日考勤行

## 构建与部署

### 后端打包

```bash
mvn clean package -DskipTests
```

产物：`target/management-0.0.1-SNAPSHOT.jar`

```bash
java -jar target/management-0.0.1-SNAPSHOT.jar
```

### 前端构建

```bash
cd Frontend
npm run build
```

产物位于 `Frontend/dist/`，可部署到 Nginx 等静态服务器。生产环境需配置反向代理，将 `/api` 转发至后端服务。

可通过环境变量指定 API 地址：

```bash
VITE_API_BASE=https://your-domain.com/api npm run build
```

## 常见问题

**1. 启动报数据库连接失败**

检查 MySQL 是否运行、`emp_sys` 是否已导入、`application.yml` 中账号密码是否正确。

**2. 登录后其他接口返回 401**

- 确认 Redis 已启动
- 请求头是否携带 `Authorization`
- Token 是否与 Redis 中存储一致

**3. Redis 连接失败**

确认 `application.yml` 中 Redis 配置位于 `spring.data.redis` 下，而不是嵌套在 `datasource` 内。

**4. 头像上传失败**

检查阿里云 OSS 配置是否正确；本地开发可先跳过头像功能，不影响其他模块。

**5. 前端接口 404**

确认后端已在 `8084` 端口运行，且前端 `vite.config.js` 中代理目标地址正确。

## 许可证

本项目仅供学习与交流使用。
