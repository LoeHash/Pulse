# Pulse API 文档

本文档仅包含 `com.ruoyi.web.controller.base` 包下的管理端和客户端接口。
所有需要鉴权的接口（标记为 **需要登录**）请在 HTTP Header 中添加 `token`。

**Header 示例:**
```
token: eyJhbGciOiJIUzI1NiJ9.xxxxxxxx
```

---

## 目录

1. [客户端 - 认证接口](#客户端---认证接口)
2. [客户端 - 用户接口](#客户端---用户接口)
3. [客户端 - 课程接口](#客户端---课程接口)
4. [客户端 - 动作接口](#客户端---动作接口)
5. [客户端 - 训练记录接口](#客户端---训练记录接口)
6. [客户端 - 帖子接口](#客户端---帖子接口)
7. [客户端 - 评论接口](#客户端---评论接口)
8. [客户端 - 通知接口](#客户端---通知接口)
9. [管理端 - 用户接口](#管理端---用户接口)
10. [管理端 - 课程接口](#管理端---课程接口)
11. [管理端 - 动作接口](#管理端---动作接口)
12. [管理端 - 训练记录接口](#管理端---训练记录接口)
13. [管理端 - 帖子接口](#管理端---帖子接口)
14. [管理端 - 评论接口](#管理端---评论接口)
15. [管理端 - 通知接口](#管理端---通知接口)

---

## 客户端 - 认证接口
Base URL: `/client/auth`

| 方法 | 路径 | 描述 | 鉴权 | 参数说明 |
| :--- | :--- | :--- | :--- | :--- |
| POST | `/register` | 用户注册 | 否 | Body: `UserRegisterDTO` (username, password, ...) |
| POST | `/login` | 用户登录 | 否 | Body: `UserLoginDTO` (username, password) |
| POST | `/logout` | 用户退出登录 | 否 | - |

## 客户端 - 用户接口
Base URL: `/client/user`

| 方法 | 路径 | 描述 | 鉴权 | 参数说明 |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/profile/me` | 获取当前登录用户信息 | **需要登录** | - |
| PUT | `/profile/me` | 修改当前登录用户信息 | **需要登录** | Body: `UserUpdateDTO` |
| GET | `/profile/{id}` | 根据用户ID获取用户信息 | **需要登录** | Path: `id` |

## 客户端 - 课程接口
Base URL: `/client/course`

| 方法 | 路径 | 描述 | 鉴权 | 参数说明 |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/list` | 课程列表 | 否 | Query: `CourseClientQueryDTO` |
| GET | `/my/list` | 我的课程 | **需要登录** | - |
| GET | `/my/favorites` | 我的收藏课程 | **需要登录** | - |
| GET | `/my/likes` | 我的点赞课程 | **需要登录** | - |
| GET | `/{id}` | 课程详情 | 否 | Path: `id` |
| POST | `/{id}/participate` | 参与课程 | **需要登录** | Path: `id` |
| DELETE | `/{id}/participate` | 取消参与课程 | **需要登录** | Path: `id` |
| POST | `/{id}/favorite` | 收藏课程 | **需要登录** | Path: `id` |
| DELETE | `/{id}/favorite` | 取消收藏课程 | **需要登录** | Path: `id` |
| POST | `/{id}/like` | 点赞课程 | **需要登录** | Path: `id` |
| DELETE | `/{id}/like` | 取消点赞课程 | **需要登录** | Path: `id` |

## 客户端 - 动作接口
Base URL: `/client/action`

| 方法 | 路径 | 描述 | 鉴权 | 参数说明 |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/list` | 动作列表 | 否 | Query: `Action` (支持分页) |
| GET | `/{id}` | 动作详情 | 否 | Path: `id` |

## 客户端 - 训练记录接口
Base URL: `/client/workout-record`

| 方法 | 路径 | 描述 | 鉴权 | 参数说明 |
| :--- | :--- | :--- | :--- | :--- |
| POST | `/start` | 开始训练 | **需要登录** | Body: `WorkoutRecordStartDTO` |
| GET | `/current` | 查询当前未结束训练 | **需要登录** | - |
| GET | `/page` | 分页查询我的训练记录 | **需要登录** | Query: `WorkoutRecordQueryDTO` |
| GET | `/stat/summary` | 我的训练汇总统计 | **需要登录** | Query: `startDate`, `endDate` |
| GET | `/stat/daily` | 我的训练按日统计 | **需要登录** | Query: `startDate`, `endDate` |
| GET | `/stat/course` | 我的训练课程分布 | **需要登录** | Query: `startDate`, `endDate` |
| GET | `/{id}` | 查询我的训练记录详情 | **需要登录** | Path: `id` |
| PUT | `/{id}/finish` | 结束训练 | **需要登录** | Path: `id`, Body: `WorkoutRecordFinishDTO` |
| DELETE | `/{id}` | 删除我的训练记录 | **需要登录** | Path: `id` |

## 客户端 - 帖子接口
Base URL: `/client/forum/post`

| 方法 | 路径 | 描述 | 鉴权 | 参数说明 |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/list` | 分页查询帖子列表 | 否 | Query: `Post` |
| GET | `/list/{uid}` | 查询指定用户发布的帖子 | 否 | Path: `uid` |
| POST | `/search` | 多条件搜索帖子 | 否 | Body: `PostQueryDTO` |
| GET | `/me` | 查询当前登录用户发布的帖子 | **需要登录** | - |
| POST | `/` | 发布新帖子 | **需要登录** | Body: `PostDTO` |
| GET | `/{id}` | 获取帖子详情 | 否 | Path: `id` |
| GET | `/{id}/comment` | 获取指定帖子的评论列表 | 否 | Path: `id` |

## 客户端 - 评论接口
Base URL: `/client/forum/comment`

| 方法 | 路径 | 描述 | 鉴权 | 参数说明 |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/post/{pid}` | 获取帖子的评论列表 | 否 | Path: `pid` |
| POST | `/` | 新增评论 | **需要登录** | Body: `CommentDTO` |
| GET | `/{cid}` | 获取评论详情 | 否 | Path: `cid` |
| DELETE | `/{id}` | 删除评论 | **需要登录** | Path: `id` |

## 客户端 - 通知接口
Base URL: `/client/notify`

| 方法 | 路径 | 描述 | 鉴权 | 参数说明 |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/unread/count` | 获取未读消息数量 | 否 | - |
| GET | `/list` | 获取未读消息列表 | 否 | - |
| GET | `/read/list` | 获取已读消息列表 | 否 | - |
| GET | `/{nid}` | 获取消息详情，并标记为已读 | 否 | Path: `nid` |

*(注：通知接口虽未明确标注 @RequireRole，但代码逻辑中通过 UserHolder 获取当前用户，暗示需要登录状态)*

---

## 管理端 - 用户接口
Base URL: `/admin/user`
**鉴权: 全部需要最高权限 (ROLE_HIGHEST)**

| 方法 | 路径 | 描述 | 参数说明 |
| :--- | :--- | :--- | :--- |
| GET | `/page` | 分页查询用户 | Query: `UserPageQueryDTO` |
| POST | `/` | 新增用户 | Body: `UserCreateDTO` |
| GET | `/{id}` | 获取用户详情 | Path: `id` |
| PUT | `/{id}` | 修改用户 | Path: `id`, Body: `UserAdminUpdateDTO` |
| DELETE | `/{id}` | 删除用户 | Path: `id` |

## 管理端 - 课程接口
Base URL: `/admin/course`
**鉴权: 全部需要最高权限 (ROLE_HIGHEST)**

| 方法 | 路径 | 描述 | 参数说明 |
| :--- | :--- | :--- | :--- |
| GET | `/list` | 查询课程列表 | Query: `CoursePageQueryDTO` |
| GET | `/page` | 分页查询课程 | Query: `CoursePageQueryDTO` |
| POST | `/` | 新增课程 | Body: `CourseCreateDTO` |
| GET | `/{id}` | 获取课程详情 | Path: `id` |
| PUT | `/{id}` | 修改课程 | Path: `id`, Body: `CourseUpdateDTO` |
| DELETE | `/{id}` | 删除课程 | Path: `id` |
| GET | `/{id}/participation/summary` | 查询课程参与概览 | Path: `id` |
| GET | `/{id}/participation/page` | 查询课程参与明细 | Path: `id` |

## 管理端 - 动作接口
Base URL: `/admin/action`
**鉴权: 全部需要最高权限 (ROLE_HIGHEST)**

| 方法 | 路径 | 描述 | 参数说明 |
| :--- | :--- | :--- | :--- |
| GET | `/list` | 查询动作列表 | Query: `Action` |
| POST | `/` | 新增动作 | Body: `Action` |
| PUT | `/` | 修改动作 | Body: `Action` |
| GET | `/{id}` | 获取动作详细信息 | Path: `id` |
| DELETE | `/{ids}` | 删除动作 | Path: `ids` (List) |

## 管理端 - 训练记录接口
Base URL: `/admin/workout-record`
**鉴权: 全部需要最高权限 (ROLE_HIGHEST)**

| 方法 | 路径 | 描述 | 参数说明 |
| :--- | :--- | :--- | :--- |
| GET | `/list` | 查询训练记录列表 | Query: `WorkoutRecordQueryDTO` |
| GET | `/page` | 分页查询训练记录 | Query: `WorkoutRecordQueryDTO` |
| POST | `/` | 新增训练记录 | Body: `WorkoutRecord` |
| GET | `/stat/overview` | 按日统计训练概览 | Query: `startDate`, `endDate` |
| GET | `/stat/overview/page` | 按日分页统计训练概览 | Query: `startDate`, `endDate` |
| GET | `/stat/summary` | 训练统计汇总 | Query: `startDate`, `endDate` |
| GET | `/stat/course-rank/page` | 课程训练排行（分页） | Query: `startDate`, `endDate` |
| GET | `/stat/user-active/page` | 用户训练活跃排行（分页） | Query: `startDate`, `endDate` |
| GET | `/stat/completion-trend/page` | 按日完课趋势（分页） | Query: `startDate`, `endDate` |
| GET | `/{id}` | 查询训练记录详情 | Path: `id` |
| PUT | `/{id}` | 修改训练记录 | Path: `id`, Body: `WorkoutRecord` |
| DELETE | `/{id}` | 删除训练记录 | Path: `id` |

## 管理端 - 帖子接口
Base URL: `/admin/post`
**鉴权: 全部需要最高权限 (ROLE_HIGHEST)**

| 方法 | 路径 | 描述 | 参数说明 |
| :--- | :--- | :--- | :--- |
| GET | `/list` | 查询帖子列表 | Query: `Post` |
| GET | `/list/{uid}` | 查询指定用户的帖子列表 | Path: `uid` |
| POST | `/export` | 导出帖子列表 | Query: `Post` |
| POST | `/` | 新增帖子 | Body: `PostDTO` |
| PUT | `/` | 修改帖子 | Body: `Post` |
| GET | `/{id}` | 获取帖子详情 | Path: `id` |
| DELETE | `/{ids}` | 删除帖子 | Path: `ids` (Array) |

## 管理端 - 评论接口
Base URL: `/admin/comment`
**鉴权: 全部需要最高权限 (ROLE_HIGHEST)**

| 方法 | 路径 | 描述 | 参数说明 |
| :--- | :--- | :--- | :--- |
| GET | `/list` | 查询评论列表 | Query: `Comment` |
| POST | `/export` | 导出评论列表 | Query: `Comment` |
| POST | `/` | 新增评论 | Body: `Comment` |
| PUT | `/` | 修改评论 | Body: `Comment` |
| GET | `/{id}` | 获取评论详情 | Path: `id` |
| DELETE | `/{ids}` | 删除评论 | Path: `ids` (Array) |

## 管理端 - 通知接口
Base URL: `/admin/notify`
**鉴权: 全部需要最高权限 (ROLE_HIGHEST)**

| 方法 | 路径 | 描述 | 参数说明 |
| :--- | :--- | :--- | :--- |
| GET | `/list` | 查询通知列表 | Query: `Notice` |
| POST | `/publish` | 发布系统通知（管理员） | Body: `NoticePublishDTO` |
| GET | `/{id}` | 获取通知详情 | Path: `id` |
| PUT | `/cancel/{id}` | 撤销通知 | Path: `id` |
| DELETE | `/{ids}` | 删除通知 | Path: `ids` (Array) |

