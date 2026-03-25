# WebSocket 协议文档（基于 socket/core 的 domain + constant）

本文档面向前端联调，依据后端当前实现整理：
- `ruoyi-system/src/main/java/com/ruoyi/system/socket/core/constant/MessageType.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/socket/core/domain/ws/*`
- 以及实际处理链路（`WsBasicEndpoint`、`DispatcherThreadPool`、`GyroAIThreadPool`、`ResultSendThreadPool`）

## 1. 连接地址

- WebSocket URL：`/ws/basic/{clientId}`
- 示例：`ws://<host>:<port>/ws/basic/frontend_001`

### 1.1 连接成功回包
连接建立后，服务端会先推送一条纯文本：

```text
connected:{clientId}
```

> 注意：这是纯文本，不是 JSON 信封。

## 2. 统一消息信封（WsMessage<T>）

后续业务消息统一使用以下 JSON 结构：

```json
{
  "type": "GYRO_WINDOW",
  "sessionId": "jakarta-session-id",
  "msgId": "msg-001",
  "ts": 1760000000000,
  "payload": {}
}
```

字段说明：
- `type`：消息类型（见下表）
- `sessionId`：会话标识，建议前端透传服务端侧会话标识（用于回包路由）
- `msgId`：消息唯一标识，建议前端生成，便于请求-响应关联
- `ts`：毫秒时间戳
- `payload`：业务载荷对象

## 3. 消息类型（MessageType）

来源：`MessageType.java`

- `GYRO_WINDOW`：客户端 -> 服务端，上报陀螺仪窗口数据
- `EXEC_TYPE`：预留类型（当前实现未处理）
- `AI_RESULT`：服务端 -> 客户端，返回识别结果

## 4. 业务载荷格式

## 4.1 GYRO_WINDOW payload（GyroWindowPayload）

```json
{
  "windowId": "w-20260321-0001",
  "samples": [
    { "t": 1760000000000, "gx": 0.11, "gy": -0.03, "gz": 0.98 },
    { "t": 1760000000020, "gx": 0.12, "gy": -0.02, "gz": 0.97 }
  ]
}
```

字段说明：
- `windowId`：窗口 ID（一次采样窗口的唯一标识）
- `samples`：采样点数组
  - `t`：采样时间（毫秒时间戳）
  - `gx`、`gy`、`gz`：三轴陀螺仪值（单位由设备侧约定，后端当前不做单位校验）

## 4.2 AI_RESULT payload（AIResult）

```json
{
  "windowId": "w-20260321-0001",
  "ts": 1760000001234,
  "result": "跑步"
}
```

字段说明：
- `windowId`：对应请求窗口 ID
- `ts`：AI 结果产生时间戳
- `result`：识别结果文本

> 当前实现中 `result` 为模拟值（固定示例：`跑步`）。

## 4.3 预留模型（MotionStatePayload）

`MotionStatePayload` 已定义，但当前主链路未实际发送：

```json
{
  "windowId": "w-xxx",
  "state": "RUNNING",
  "confidence": 0.92,
  "stale": false
}
```

建议前端先不依赖该模型，待后端明确启用再接入。

## 5. 联调时序（当前实现）

1. 前端连接 `/ws/basic/{clientId}`
2. 收到纯文本 `connected:{clientId}`
3. 前端发送 `type=GYRO_WINDOW` 的 JSON 消息
4. 服务端异步入队 -> 分发 -> AI 处理 -> 返回 `type=AI_RESULT`
5. 返回消息会尽量沿用原 `sessionId`、`msgId`（便于前端做请求响应匹配）

## 6. 请求与响应完整示例

## 6.1 上行示例（前端发送）

```json
{
  "type": "GYRO_WINDOW",
  "sessionId": "2a7f8e4d-...",
  "msgId": "msg-gyro-1001",
  "ts": 1760000000000,
  "payload": {
    "windowId": "window-1001",
    "samples": [
      { "t": 1760000000000, "gx": 0.11, "gy": -0.03, "gz": 0.98 },
      { "t": 1760000000020, "gx": 0.12, "gy": -0.02, "gz": 0.97 }
    ]
  }
}
```

## 6.2 下行示例（服务端返回）

```json
{
  "type": "AI_RESULT",
  "sessionId": "2a7f8e4d-...",
  "msgId": "msg-gyro-1001",
  "ts": 1760000001234,
  "payload": {
    "windowId": "window-1001",
    "ts": 1760000001234,
    "result": "跑步"
  }
}
```

## 7. 错误与注意事项

当前后端错误处理偏运行时异常，建议前端按以下方式兜底：

- `type` 缺失/未知：后端会抛 `unsupported message type`
- `sessionId` 缺失：结果发送阶段可能报错 `sessionId is null or empty`
- `EXEC_TYPE`：当前未处理，发送后不会产生业务回包
- WebSocket 断线后需要重连，并重新发送后续数据

建议前端最小兜底策略：
- 所有上行消息带 `msgId`
- 维护 `msgId -> 请求时间` 的本地映射，超时重发
- 解析失败或断线时按指数退避重连

## 8. 前端联调清单

- [ ] 连接成功时处理纯文本 `connected:{clientId}`
- [ ] 上行统一使用 `WsMessage` 信封
- [ ] 只发送已支持的 `GYRO_WINDOW`
- [ ] `payload.samples` 保证是数组且字段名严格匹配 `t/gx/gy/gz`
- [ ] 用 `msgId` 关联 `AI_RESULT`
- [ ] 对异常消息与断线做重试和兜底提示

## 9. 后续协议建议（可选）

为降低联调成本，建议后续升级：
- 增加统一错误消息类型（如 `ERROR`）
- 在信封中新增 `version`
- 明确 `gx/gy/gz` 单位与采样频率约束
- 明确 `EXEC_TYPE` 语义并补充示例

