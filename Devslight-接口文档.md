# Devslight 接口文档

## 1. 文档说明

本文档用于 Devslight 后端接口开发，适配 Spring Boot + Spring Security + MyBatis + MySQL。


### 1.1 基础约定

| 项目 | 约定 |
|---|---|
| 基础路径 | `/api` |
| 数据格式 | `application/json` |
| 字段命名 | JSON 使用 `camelCase`，数据库使用下划线命名 |
| 时间格式 | `yyyy-MM-dd HH:mm:ss` |
| 认证方式 | JWT |
| 请求头 | `Authorization: Bearer {token}` |
| 删除方式 | 软删除，设置 `isDeleted = 1` |

由于项目配置了 `context-path: /api`，控制器中只需要声明 `/auth/login`，实际请求地址为 `/api/auth/login`。

除登录、注册和 Swagger 接口外，其余接口默认需要登录。

## 2. 统一响应结构

### 2.1 普通成功响应

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {}
}
```

### 2.2 分页响应

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "list": [],
    "pageNum": 1,
    "pageSize": 10,
    "total": 0,
    "totalPages": 0
  }
}
```

### 2.3 错误响应

```json
{
  "code": 4001,
  "message": "仓库不存在",
  "data": null
}
```

### 2.4 状态码

| code | 含义 |
|---:|---|
| 0 | 成功 |
| 4000 | 参数错误 |
| 4001 | 资源不存在 |
| 4002 | 业务状态不允许 |
| 4003 | 用户名或密码错误 |
| 4004 | 无权访问该资源 |
| 4005 | JWT 无效或已过期 |
| 4090 | 数据重复 |
| 5000 | 系统异常 |
| 5001 | 同步任务执行失败 |
| 5002 | GitHub 接口调用失败 |
| 5003 | Git 仓库解析失败 |

## 3. 认证与用户接口

### 3.1 用户注册

`POST /api/auth/register`

无需 JWT。

请求体：

```json
{
  "username": "admin",
  "password": "123456",
  "email": "admin@example.com",
  "nickName": "管理员"
}
```

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| username | string | 是 | 4～64 位，不能重复 |
| password | string | 是 | 建议至少 6 位，服务端使用 BCrypt 加密 |
| email | string | 否 | 邮箱，不能与其他用户重复 |
| nickName | string | 否 | 昵称 |

返回：

```json
{
  "code": 0,
  "message": "注册成功",
  "data": { "userId": 1 }
}
```

### 3.2 登录

`POST /api/auth/login`

无需 JWT。

请求体：

```json
{
  "username": "admin",
  "password": "123456"
}
```

返回：

```json
{
  "code": 0,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenHead": "Bearer",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "nickName": "管理员",
      "avatarUrl": null,
      "email": "admin@example.com",
      "roleCode": "ADMIN"
    }
  }
}
```

登录校验 `sys_user.status = 1` 且 `is_deleted = 0`。数据库中的 `password` 字段只保存加密值，不保存明文密码。

### 3.3 获取当前用户

`GET /api/auth/me`

请求头：`Authorization: Bearer {token}`

返回当前登录用户的昵称、头像、邮箱、手机号、角色和登录时间。

### 3.4 修改个人资料

`PUT /api/auth/profile`

请求体：

```json
{
  "nickName": "Devslight",
  "avatarUrl": "https://example.com/avatar.png",
  "email": "user@example.com",
  "phone": "13800000000"
}
```

### 3.5 修改密码

`PUT /api/auth/password`

请求体：

```json
{
  "oldPassword": "123456",
  "newPassword": "12345678"
}
```

修改成功后建议要求前端重新登录并获取新 JWT。

## 4. GitHub 账号接口

GitHub 账号通过 `github_account.user_id` 关联当前系统用户。账号接口只能操作当前登录用户自己的账号。

### 4.1 绑定 GitHub 账号

`POST /api/github-accounts`

请求体：

```json
{
  "accessToken": "ghp_xxx",
  "accountType": "PERSONAL",
  "remark": "个人 GitHub 账号"
}
```

服务端使用 Token 调用 GitHub `/user` 接口获取并保存 `githubLogin`、`githubUserId`、`githubName`、`githubEmail`、`avatarUrl` 和 `profileUrl`。不要让前端直接提交这些可伪造的 GitHub 信息。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| accessToken | string | 是 | GitHub Personal Access Token |
| accountType | string | 否 | `PERSONAL` 或 `ORG` |
| remark | string | 否 | 备注 |

返回：

```json
{
  "code": 0,
  "message": "GitHub 账号绑定成功",
  "data": {
    "id": 1,
    "githubLogin": "octocat",
    "githubUserId": 583231,
    "avatarUrl": "https://avatars.githubusercontent.com/u/583231",
    "tokenStatus": 1
  }
}
```

### 4.2 GitHub 账号列表

`GET /api/github-accounts`

返回当前用户未删除的 GitHub 账号。出于安全原因，列表中不得返回完整 `accessToken`，最多返回是否有效及脱敏信息。

### 4.3 GitHub 账号详情

`GET /api/github-accounts/{id}`

只能查询当前用户拥有的账号，返回字段与列表一致。

### 4.4 更新 GitHub Token

`PUT /api/github-accounts/{id}`

请求体：

```json
{
  "accessToken": "ghp_new_token",
  "remark": "更新后的 Token"
}
```

更新后重新调用 GitHub `/user` 校验 Token，成功后设置 `tokenStatus = 1`，失败则设置为 `0`。

### 4.5 删除 GitHub 账号

`DELETE /api/github-accounts/{id}`

执行软删除。若仍有仓库使用该账号，建议返回 `4002`，要求先修改仓库的账号关联。

## 5. 仓库接口

### 5.1 新增仓库

`POST /api/repositories`

请求体：

```json
{
  "repoName": "devslight-demo",
  "repoFullName": "example/devslight-demo",
  "repoUrl": "https://github.com/example/devslight-demo.git",
  "platformType": "GITHUB",
  "githubAccountId": 1,
  "defaultBranch": "main",
  "visibility": "PUBLIC",
  "accessMode": "TOKEN",
  "remark": "演示仓库"
}
```

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| repoName | string | 是 | 仓库名称 |
| repoFullName | string | 否 | GitHub 格式的 `owner/repository` |
| repoUrl | string | 是 | Git 或 GitHub 地址 |
| platformType | string | 是 | `GITHUB`、`GITLAB`、`LOCAL` |
| githubAccountId | long | 条件必填 | GitHub 私有仓库或 Token/OAuth 模式必须传 |
| defaultBranch | string | 否 | 默认 `main` |
| visibility | string | 否 | `PUBLIC` 或 `PRIVATE` |
| accessMode | string | 否 | `TOKEN`、`OAUTH` 或 `LOCAL` |
| localPath | string | 条件必填 | `LOCAL` 模式必须传本地路径 |
| remark | string | 否 | 备注 |

校验规则：

- `platformType = GITHUB` 且 `accessMode = TOKEN/OAUTH` 时，`githubAccountId` 必须属于当前用户。
- `platformType = LOCAL` 时不需要 `githubAccountId`，但必须传 `localPath`。
- 公开 GitHub 仓库且不调用授权 API 时，可以不传 `githubAccountId`。
- 同一个用户不能重复登记相同的 `repoFullName`。

### 5.2 仓库分页列表

`GET /api/repositories?pageNum=1&pageSize=10&keyword=dev&platformType=GITHUB&status=1`

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| pageNum | int | 否 | 默认 1 |
| pageSize | int | 否 | 默认 10，最大 100 |
| keyword | string | 否 | 名称或全名关键字 |
| platformType | string | 否 | 平台类型 |
| status | int | 否 | 0 禁用，1 正常 |

只返回 `ownerUserId` 等于当前用户且 `isDeleted = 0` 的仓库。

### 5.3 仓库详情

`GET /api/repositories/{repositoryId}`

返回仓库基础信息、GitHub 账号摘要、最近同步时间、数据统计数量和当前健康度。

### 5.4 修改仓库

`PUT /api/repositories/{repositoryId}`

请求体可包含：`repoName`、`githubAccountId`、`defaultBranch`、`visibility`、`accessMode`、`localPath`、`remark`。

修改关联账号时，必须重新执行归属校验。

### 5.5 删除仓库

`DELETE /api/repositories/{repositoryId}`

执行软删除，不物理删除该仓库已有的提交、分支和分析历史。

### 5.6 仓库基础统计

`GET /api/repositories/{repositoryId}/statistics`

返回：

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "commitCount": 356,
    "branchCount": 8,
    "contributorCount": 12,
    "pullRequestCount": 42,
    "fileChangeCount": 1560,
    "lastSyncTime": "2026-08-06 14:00:00"
  }
}
```

## 6. 同步任务接口

`sync_task` 表表示一次数据采集或分析执行记录，不是 Git 数据本身。它用于记录谁在什么时候对哪个仓库发起了什么类型的同步、执行状态和结果数量。

### 6.1 发起同步

`POST /api/repositories/{repositoryId}/sync`

请求体：

```json
{
  "taskType": "FULL",
  "syncScope": "ALL",
  "triggerAnalysis": true
}
```

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| taskType | string | 是 | `FULL`、`INCREMENTAL` |
| syncScope | string | 是 | `ALL`、`COMMITS`、`PRS`、`BRANCHES` |
| triggerAnalysis | boolean | 否 | 同步成功后是否计算指标，默认 true |

返回：

```json
{
  "code": 0,
  "message": "同步任务已创建",
  "data": {
    "taskId": 1001,
    "repositoryId": 12,
    "taskStatus": "PENDING"
  }
}
```

接口只创建任务并返回，不建议在 HTTP 请求线程中同步执行完整仓库扫描。

### 6.2 查询同步任务详情

`GET /api/sync-tasks/{taskId}`

返回任务状态、开始时间、结束时间、提交数、PR 数、分支数和错误信息。

状态：`PENDING`、`RUNNING`、`SUCCESS`、`FAILED`。

### 6.3 查询仓库同步任务

`GET /api/repositories/{repositoryId}/sync-tasks?pageNum=1&pageSize=10`

按创建时间倒序返回当前仓库的同步任务。

### 6.4 取消同步任务

`POST /api/sync-tasks/{taskId}/cancel`

只有 `PENDING` 或可中断的 `RUNNING` 任务允许取消。

## 7. 分析与健康度接口

### 7.1 仓库总览

`GET /api/repositories/{repositoryId}/overview?timeWindowDays=30`

返回仓库名称、统计窗口、综合分数、风险等级、代码分、人员分、过程分和开放告警数。

### 7.2 获取最新健康度

`GET /api/repositories/{repositoryId}/health-score?timeWindowDays=30`

示例：

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "repositoryId": 12,
    "snapshotDate": "2026-08-06",
    "timeWindowDays": 30,
    "totalScore": 82.5,
    "codeScore": 80.0,
    "peopleScore": 78.5,
    "processScore": 86.0,
    "riskLevel": "YELLOW",
    "scoreVersion": "v1"
  }
}
```

分数范围 `0～100`，风险等级建议：`GREEN`（85～100）、`YELLOW`（70～84）、`RED`（0～69）。

### 7.3 重新计算健康度

`POST /api/repositories/{repositoryId}/analysis/health-score`

请求体：

```json
{
  "timeWindowDays": 30
}
```

计算完成后保存分析快照并返回快照 ID。后续可以改为异步任务，但第一版允许同步执行小型仓库分析。

### 7.4 健康度历史趋势

`GET /api/repositories/{repositoryId}/health-score/history?startDate=2026-07-01&endDate=2026-08-06`

返回每日或每次分析的 `snapshotDate`、`totalScore` 和 `riskLevel`。

### 7.5 指标趋势

`GET /api/repositories/{repositoryId}/metrics/trend?metricCode=CHURN_RATE&startDate=2026-07-01&endDate=2026-08-06`

支持指标编码：

`COMMIT_FREQUENCY`、`CHURN_RATE`、`HOT_FILE_CONCENTRATION`、`BUS_FACTOR`、`CONTRIBUTION_BALANCE`、`PR_CYCLE_TIME`、`REVIEW_DELAY`、`MERGE_FREQUENCY`。

### 7.6 指标快照

`GET /api/repositories/{repositoryId}/metrics/snapshot?date=2026-08-06&timeWindowDays=30`

返回当前统计窗口内所有基础指标值，供前端展示指标卡片。

## 8. 开发者与文件分析接口

### 8.1 开发者列表

`GET /api/repositories/{repositoryId}/contributors?pageNum=1&pageSize=10&keyword=zhang&timeWindowDays=30`

返回开发者提交数、修改文件数、活跃天数、贡献比例、主要模块和风险分数。

### 8.2 开发者详情

`GET /api/repositories/{repositoryId}/contributors/{contributorId}?timeWindowDays=30`

返回开发者基本信息、提交趋势、修改类型、常改文件、协作开发者和贡献比例。

### 8.3 热文件列表

`GET /api/repositories/{repositoryId}/hot-files?pageNum=1&pageSize=20&timeWindowDays=30`

返回文件路径、修改次数、修改开发者数、代码变更量、风险分数和主要修改者。

### 8.4 文件详情

`GET /api/repositories/{repositoryId}/files/detail?filePath=src/main/java/App.java&timeWindowDays=30`

文件路径需要进行 URL 编码。返回文件变更趋势、提交记录、贡献者分布和风险原因。

### 8.5 协作关系图

`GET /api/repositories/{repositoryId}/collaboration-graph?timeWindowDays=30`

返回：

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "nodes": [
      { "id": "25", "name": "zhangsan", "commitCount": 56 }
    ],
    "edges": [
      { "source": "25", "target": "26", "weight": 12 }
    ]
  }
}
```

## 9. 分支与时间轴接口

### 9.1 分支列表

`GET /api/repositories/{repositoryId}/branches?status=ACTIVE`

返回分支名称、类型、是否默认分支、头提交、最近提交时间和状态。

### 9.2 Commit 时间轴

`GET /api/repositories/{repositoryId}/timeline?startDate=2026-07-01&endDate=2026-08-06&branchName=main`

返回按时间排序的提交、分支创建、分支合并和标签事件。

### 9.3 Commit 列表

`GET /api/repositories/{repositoryId}/commits?pageNum=1&pageSize=20&branchName=main&author=zhangsan`

返回提交哈希、作者、提交说明、提交时间、父提交数、修改文件数、新增行数、删除行数和是否合并提交。

## 10. 风险告警接口

### 10.1 告警列表

`GET /api/repositories/{repositoryId}/alerts?pageNum=1&pageSize=10&status=OPEN&level=HIGH`

告警状态：`OPEN`、`ACKNOWLEDGED`、`RESOLVED`、`IGNORED`。

告警等级：`HIGH`、`MEDIUM`、`LOW`。

告警类型示例：

- `HIGH_CHURN_FILE`：文件反复修改
- `SINGLE_MAINTAINER`：单人维护风险
- `REVIEW_DELAY`：PR 长时间未评审
- `HOT_FILE`：热点文件集中度过高
- `LARGE_CHANGE`：大规模变更

### 10.2 告警详情

`GET /api/alerts/{alertId}`

返回告警类型、等级、标题、描述、关联文件、关联开发者、指标值、阈值、建议和创建时间。

### 10.3 更新告警状态

`PUT /api/alerts/{alertId}/status`

请求体：

```json
{
  "status": "ACKNOWLEDGED",
  "remark": "已安排其他开发者参与维护"
}
```

### 10.4 手动生成告警

`POST /api/repositories/{repositoryId}/analysis/alerts`

根据最近一次指标快照重新执行规则检测，生成或更新告警。

## 11. GitHub Webhook 接口

### 11.1 接收 Webhook

`POST /api/webhooks/github`

该接口不使用用户 JWT，而是使用 GitHub Webhook Secret 校验请求签名 `X-Hub-Signature-256`。

支持事件：`push`、`pull_request`、`pull_request_review`。

处理原则：

1. 校验签名。
2. 根据仓库全名匹配 `repository`。
3. 创建 `WEBHOOK` 类型同步任务。
4. 快速返回 HTTP 200，具体处理放到任务线程中。

## 12. 权限与安全要求

- 任何仓库、GitHub 账号、同步任务和分析接口都必须校验当前用户归属。
- 不允许只根据 URL 中的 ID 直接查询并返回数据。
- GitHub Token 不得在接口响应中返回明文。
- 日志中不得打印 Token、密码和完整 JWT。
- `password` 字段名称可以保留，但实际值必须是 BCrypt 密文。
- 软删除数据统一追加 `is_deleted = 0` 查询条件。
- GitHub Token 建议加密后存储，至少要限制数据库字段和日志访问权限。

## 13. 第一版实现顺序

1. `POST /api/auth/register`
2. `POST /api/auth/login`
3. `GET /api/auth/me`
4. `POST /api/github-accounts`
5. `GET /api/github-accounts`
6. `POST /api/repositories`
7. `GET /api/repositories`
8. `POST /api/repositories/{repositoryId}/sync`
9. `GET /api/sync-tasks/{taskId}`
10. `GET /api/repositories/{repositoryId}/overview`
11. `GET /api/repositories/{repositoryId}/health-score`
12. `GET /api/repositories/{repositoryId}/contributors`
13. `GET /api/repositories/{repositoryId}/hot-files`
14. `GET /api/repositories/{repositoryId}/alerts`

先完成前八个接口即可形成“用户登录、账号绑定、仓库登记、任务创建”的可运行闭环；分析接口可以在同步数据落库后继续实现。
