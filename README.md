# VelocityVersionTinmoli

一个轻量级的 Velocity 3.4 代理插件，用于自定义服务器列表中显示的版本名称。

## 简介

VelocityVersionTinmoli 允许服务器管理员自定义 Minecraft 服务器列表中显示的版本信息。通过简单的 TOML 配置文件，您可以将默认的 "Velocity 3.5" 替换为任何自定义文本，并支持 MiniMessage 格式来添加颜色和样式。

**特性：**
- 简单的 TOML 配置文件
- 轻量级设计，无性能影响
- 自动创建默认配置
- 完善的错误处理和日志记录

## 安装说明

### 前置要求

- Velocity 3.4.0 或更高版本
- Java 21 或更高版本

### 安装步骤

1. 从 [Releases](../../releases) 页面下载最新的 `VelocityVersionTinmoli-1.0.0.jar` 文件

2. 将 JAR 文件放入 Velocity 服务器的 `plugins` 目录：
   ```
   velocity-server/
   └── plugins/
       └── VelocityVersionTinmoli-1.0.0.jar
   ```

3. 启动或重启 Velocity 服务器

4. 插件将自动在 `plugins/velocityversiontinmoli/` 目录下创建默认配置文件

## 配置说明

### 配置文件位置

配置文件位于：`plugins/velocityversiontinmoli/config.toml`

### 配置文件格式

```toml
# VelocityVersionTinmoli 配置文件
# 自定义服务器列表中显示的版本名称

# 示例：
#   version_name = "Velocity 1.8.x-1.21.11"
version_name = "Velocity 1.8.x-1.21.11"
```


### 应用配置更改

修改配置文件后，需要重启 Velocity 服务器才能使更改生效。

## 构建说明

### 前置要求

- JDK 21 或更高版本
- Gradle 8.0 或更高版本（或使用包含的 Gradle Wrapper）

### 构建步骤

1. 克隆或下载项目源代码

2. 在项目根目录下运行构建命令：

   **Windows:**
   ```bash
   gradlew.bat clean build
   ```

   **Linux/Mac:**
   ```bash
   ./gradlew clean build
   ```

3. 构建完成后，JAR 文件将位于：
   ```
   build/libs/VelocityVersionTinmoli-1.0.0.jar
   ```

### 构建插件（便捷方式）

使用 `buildPlugin` 任务可以构建并将 JAR 复制到便捷位置：

```bash
gradlew.bat buildPlugin
```

插件 JAR 将被复制到：`build/plugins/VelocityVersionTinmoli-1.0.0.jar`

### 运行测试

```bash
gradlew.bat test
```

测试报告将生成在：`build/reports/tests/test/index.html`

### 代码覆盖率报告

```bash
gradlew.bat jacocoTestReport
```

覆盖率报告将生成在：`build/reports/jacoco/test/html/index.html`

## 工作原理

1. **启动阶段**：插件加载时读取 `config.toml` 配置文件，如果文件不存在则自动创建默认配置

2. **运行阶段**：当玩家 Ping 服务器时，插件拦截 `ProxyPingEvent` 事件，将版本信息替换为配置的自定义版本名称

3. **保持兼容**：插件仅修改版本信息，保持 MOTD、玩家列表、服务器图标等其他信息不变

## 故障排除

### 配置文件未生效

- 确保配置文件位于正确的位置：`plugins/velocityversiontinmoli/config.toml`
- 检查配置文件格式是否正确（TOML 语法）
- 修改配置后需要重启 Velocity 服务器

### 版本名称显示异常

- 检查 MiniMessage 标签是否正确闭合
- 某些客户端可能不支持所有 MiniMessage 格式
- 查看服务器日志中是否有错误信息

### 插件未加载

- 确保使用 Velocity 3.5.0 或更高版本
- 确保服务器运行在 Java 21 或更高版本
- 检查 `logs/latest.log` 中的错误信息

## 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件。

## 支持

如有问题或建议，请在 [GitHub Issues](../../issues) 页面提交。

## 更新日志

### v1.0.0
- 初始版本发布
- 支持自定义版本名称
- 自动配置文件创建
- 完善的错误处理
