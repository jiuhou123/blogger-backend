# JWT (JSON Web Token) 学习文档

## 1. 什么是 JWT？
JWT，全称 JSON Web Token，是一个开放标准（[RFC 7519](https://datatracker.ietf.org/doc/html/rfc7519)），它定义了一种紧凑且自包含的方式，用于在双方之间安全地传输信息。这些信息以 JSON 对象的形式表示，并且可以使用数字签名进行验证，从而确保信息的完整性和真实性。

JWT 通常用于**身份认证和授权**。当用户成功登录后，服务器会生成一个 JWT 并发送给客户端。客户端收到 JWT 后，将其保存在本地（例如 localStorage 或 Cookie 中），并在后续的请求中将其添加到请求头中发送给服务器。服务器接收到 JWT后，验证其有效性，并根据 JWT 中的信息（例如用户 ID、角色等）来确定用户的身份和权限，而无需频繁查询数据库。

**关键词：** 紧凑、自包含、JSON、数字签名、身份认证、授权。

## 2. JWT 的结构

一个 JWT 由三个部分组成，它们之间用圆点（`.`）分隔：

`Header.Payload.Signature`

例如：

`eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c`

### 2.1 Header（头部）

Header 通常包含两部分信息：

*   `alg` (Algorithm): 签名算法，例如 HS256 (HMAC SHA256) 或 RS256 (RSA SHA256)。
*   `typ` (Type): Token 类型，通常是 "JWT"。

Header 的 JSON 结构示例：

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

然后，这个 JSON 对象会被进行 Base64 Url Safe 编码，形成 JWT 的第一部分。

### 2.2 Payload（载荷）

Payload 包含 Token 的主体信息，也称为 **Claims（声明）**。Claims 是关于实体（通常是用户）和其他元数据（例如过期时间、签发者等）的陈述。Claims 分为三种类型：

*   **Registered Claims (注册声明)**: 预定义的一些声明，它们不是强制的，但建议使用，以提供一套有用的、可互操作的 Claims。例如：
    *   `iss` (Issuer): 签发者
    *   `exp` (Expiration Time): 过期时间戳
    *   `sub` (Subject): 主题，通常是用户 ID
    *   `aud` (Audience): 接收 JWT 的一方
    *   `iat` (Issued At): 签发时间戳
    *   `jti` (JWT ID): JWT 的唯一标识符
*   **Public Claims (公共声明)**: 可以由使用 JWT 的各方自行定义，但为了避免冲突，应在 [IANA JSON Web Token Claims 注册表](https://www.iana.org/assignments/jwt/jwt.xhtml#claims) 中注册，或者定义为包含命名空间的 URI。
*   **Private Claims (私有声明)**: 在同意使用它们的各方之间共享的自定义声明。它们既不是注册声明也不是公共声明。例如，你可以添加一个 `role` 声明来表示用户的角色。

Payload 的 JSON 结构示例：

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022,
  "role": "admin"
}
```

Payload 的 JSON 对象也会被进行 Base64 Url Safe 编码，形成 JWT 的第二部分。

### 2.3 Signature（签名）

Signature 部分用于验证 JWT 的发送者，并确保信息在传输过程中没有被篡改。签名是使用 Header 中指定的算法，结合 Base64 编码后的 Header、Base64 编码后的 Payload 以及一个**密钥 (Secret)** 生成的。

签名计算的伪代码：

```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

对于 HS256 算法，Signature 是通过将 Base64 编码后的 Header 和 Payload 用圆点连接组成的字符串，使用一个密钥进行 HMACSHA256 加密生成的。对于 RS256 算法，则使用私钥进行 RSA 签名。

Signature 的生成方式确保了：

*   知道密钥的第三方可以验证 Token 是否是由知道密钥的服务器签发的。
*   可以验证 Token 的 Header 和 Payload 在传输过程中是否被篡改。

## 3. JWT 的工作原理

一个典型的基于 JWT 的认证流程如下：

1.  **用户登录**：用户通过用户名和密码向服务器发送登录请求。
2.  **服务器验证**：服务器验证用户的身份信息。
3.  **生成 JWT**：验证成功后，服务器生成一个 JWT，其中包含用户的身份信息（如用户 ID）和一些其他 Claims（如过期时间）。使用密钥对 Header 和 Payload 进行签名。
4.  **发送 JWT**：服务器将生成的 JWT 作为响应发送给客户端。
5.  **客户端存储**：客户端接收到 JWT，将其存储在浏览器端（如 localStorage、Session Storage 或 Cookie）。
6.  **后续请求携带 JWT**：客户端在后续访问需要认证的接口时，将 JWT 放置在 HTTP 请求的 `Authorization` 头中，通常以 `Bearer` 方案携带：`Authorization: Bearer <token>`。
7.  **服务器验证 JWT**：服务器接收到请求，从 `Authorization` 头中提取 JWT。使用**相同的密钥**验证 JWT 的签名。
8.  **解析 JWT**：验证签名成功后，服务器解析 JWT 的 Payload，获取其中的用户信息和其他 Claims。
9.  **身份识别与权限校验**：服务器根据 Payload 中的信息识别用户身份，并进行权限校验。
10. **返回响应**：服务器处理请求并返回响应给客户端。

## 4. JWT 的优缺点

### 优点：

*   **无状态 (Stateless)**: 服务器不需要存储 Session 信息，减轻了服务器的存储压力。特别适用于分布式系统和微服务架构。
*   **可伸缩性高**: 因为无状态，可以轻松扩展服务实例。
*   **信息自包含**: Payload 中包含了用户的基本信息，服务器可以直接从 Token 中获取，减少了数据库查询次数。
*   **跨域友好**: JWT 通常通过 HTTP 头传递，不受 Cookie 的同源策略限制，方便跨域调用。
*   **标准**: JWT 是一个开放标准，有各种语言的库支持。

### 缺点：

*   **安全性**：Payload 是 Base64 编码的，虽然不可读性差，但不是加密，**敏感信息不应直接放在 Payload 中**。私有声明的安全性取决于密钥的强度和保密性。
*   **不可撤销**: 一旦 JWT 签发，直到过期前都是有效的。如果用户被禁用或需要提前吊销 Token，需要额外的机制（例如黑名单）。
*   **Token 体积**: 如果 Payload 包含大量信息，Token 体积会变大，增加请求开销。
*   **密钥管理**: 密钥泄露会导致严重的安全问题，需要妥善保管。

## 5. 安全注意事项

*   **使用强密钥**：密钥必须足够复杂且保密。
*   **设置合理的过期时间**：不宜过长，以降低 Token 泄露的风险。
*   **不要在 Payload 中存放敏感信息**：Payload 是 Base64 编码，可以轻易解码查看。
*   **使用 HTTPS**：确保 Token 在传输过程中的安全。
*   **实现 Token 刷新机制**：使用短生命周期的 Access Token 和长生命周期的 Refresh Token 机制，提高安全性并改善用户体验。
*   **考虑 Token 吊销/黑名单机制**：虽然 JWT 无状态，但在需要紧急禁用用户或强制下线时，黑名单是必要的。

## 6. 总结

JWT 是一种高效且灵活的跨平台身份认证和授权方案，特别适合现代分布式和微服务架构。理解其结构和工作原理，并注意相关的安全风险，可以帮助你更好地应用 JWT 构建安全的系统。

---

希望这份文档能帮助你理解 JWT 的核心概念！如果你在学习或开发过程中遇到具体问题（例如代码实现、安全配置等），欢迎随时提问！ 