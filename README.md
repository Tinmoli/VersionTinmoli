# VersionTinmoli

一个轻量级的代理服务器插件，用于自定义服务器列表中显示的版本名称。

## 支持的平台

- ✅ **Velocity 3.5+** - 已实现（需要 Java 21+）
- ✅ **BungeeCord 1.16+** - 已实现（支持 Java 17+）

## 项目结构

```
VersionTinmoli/
├── velocity/          # Velocity 实现
│   ├── src/
│   ├── build.gradle
│   └── README.md
├── bungeecord/        # BungeeCord 实现
│   ├── src/
│   ├── build.gradle
│   └── README.md
├── build.gradle       # 根构建文件
├── settings.gradle    # 多模块配置
└── README.md          # 本文件
```

## 简介

VersionTinmoli 允许服务器管理员自定义 Minecraft 服务器列表中显示的版本信息。通过简单的 TOML 配置文件，您可以将默认的版本信息替换为任何自定义文本。

**特性：**
- 简单的 TOML 配置文件
- 轻量级设计，无性能影响
- 自动创建默认配置
- 完善的错误处理和日志记录
- 多平台支持（Velocity 和 BungeeCord）

## 快速开始

### Velocity

1. 从 [Releases](https://github.com/Tinmoli/VersionTinmoli/releases) 下载 `versiontinmoli-velocity-1.0.1.jar`
2. 放入 Velocity 的 `plugins/` 目录
3. 重启服务器
4. 编辑 `plugins/versiontinmoli/config.toml` 配置文件
5. 再次重启服务器使配置生效

详细说明请查看 [velocity/README.md](velocity/README.md)

### BungeeCord

1. 从 [Releases](https://github.com/Tinmoli/VersionTinmoli/releases) 下载 `versiontinmoli-bungeecord-1.0.1.jar`
2. 放入 BungeeCord 的 `plugins/` 目录
3. 重启服务器
4. 编辑 `plugins/VersionTinmoli/config.toml` 配置文件
5. 再次重启服务器使配置生效

详细说明请查看 [bungeecord/README.md](bungeecord/README.md)

## 配置说明

### 配置文件位置

- **Velocity**: `plugins/versiontinmoli/config.toml`
- **BungeeCord**: `plugins/VersionTinmoli/config.toml`

### 配置文件格式

```toml
# VersionTinmoli 配置文件
# 自定义服务器列表中显示的版本名称

# 示例：
#   version_name = "Velocity 1.8.x-1.21.11"
version_name = "Velocity 1.8.x-1.21.11"
```

### 配置选项

| 选项 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `version_name` | String | 平台默认值 | 在服务器列表中显示的版本名称 |

### 配置示例

```toml
# 简单文本
version_name = "我的服务器 1.20"

# 版本范围
version_name = "Minecraft 1.8.x-1.21.11"

# 自定义名称
version_name = "Premium Network v1.0"
```

## 构建说明

### 前置要求

- JDK 21 或更高版本
- Gradle 8.0 或更高版本（或使用包含的 Gradle Wrapper）

### 构建所有模块

```bash
# Windows
gradlew.bat clean build

# Linux/Mac
./gradlew clean build
```

### 构建特定模块

```bash
# 仅构建 Velocity
gradlew.bat :velocity:build

# 仅构建 BungeeCord
gradlew.bat :bungeecord:build
```

### 构建输出

构建后的 JAR 文件位于：
- Velocity: `velocity/build/libs/versiontinmoli-velocity-1.0.1.jar`
- BungeeCord: `bungeecord/build/libs/versiontinmoli-bungeecord-1.0.1.jar`

### 便捷构建任务

使用 `buildPlugin` 任务会将所有 JAR 复制到 `build/plugins/` 目录：

```bash
gradlew.bat buildPlugin
```

## 系统要求

### Velocity
- Velocity 3.0 或更高版本
- Java 21 或更高版本

### BungeeCord
- BungeeCord 1.16 或更高版本
- Java 17 或更高版本（推荐 Java 21）

## 故障排除

### 配置文件未生效

- 确保配置文件位于正确的位置
- 检查配置文件格式是否正确（TOML 语法）
- 修改配置后需要重启代理服务器

### 版本名称显示异常

- 检查配置文件中的版本名称是否正确
- 查看服务器日志中是否有错误信息

### 插件未加载

- **Velocity**: 确保使用 Velocity 3.5.0+ 和 Java 21+
- **BungeeCord**: 确保使用 BungeeCord 1.16+ 和 Java 17+
- 检查日志文件中的错误信息

## 开发

### 项目规范

- Velocity: Java 21
- BungeeCord: Java 17+ 兼容（使用 Java 21 编译）
- 遵循现有代码风格
- 为新功能编写测试

## 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件。

## 支持

如有问题或建议，请在 [GitHub Issues](https://github.com/Tinmoli/VersionTinmoli/issues) 页面提交。

## 更新日志

### v1.0.1
- 添加 BungeeCord 支持
- BungeeCord 版本支持 Java 17+
- 移除 MiniMessage 相关代码和文档
- 优化项目结构为多模块

### v1.0.0
- 初始版本发布
- 支持 Velocity 3.5+
- 支持 BungeeCord 1.16+
- 支持自定义版本名称
- 自动配置文件创建
- 完善的错误处理
