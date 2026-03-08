# VersionTinmoli - Velocity

Velocity 3.5 代理插件实现。

## 系统要求

- Velocity 3.5.0 或更高版本
- Java 21 或更高版本

## 构建

在项目根目录运行：

```bash
gradlew.bat :velocity:build
```

或者构建所有模块：

```bash
gradlew.bat build
```

## 输出

构建后的 JAR 文件位于：
- `velocity/build/libs/versiontinmoli-velocity-1.0.0.jar`

使用 `buildPlugin` 任务会将 JAR 复制到根目录的 `build/plugins/` 文件夹：

```bash
gradlew.bat :velocity:buildPlugin
```

## 安装

1. 下载 `versiontinmoli-velocity-1.0.0.jar`
2. 放入 Velocity 的 `plugins/` 目录
3. 重启服务器
4. 编辑 `plugins/versiontinmoli/config.toml` 配置文件
5. 再次重启服务器使配置生效

## 配置

配置文件位于：`plugins/versiontinmoli/config.toml`

```toml
# VersionTinmoli 配置文件
# 自定义服务器列表中显示的版本名称

version_name = "Velocity 1.8.x-1.21.11"
```

### 配置示例

```toml
# 简单文本
version_name = "我的服务器 1.20"

# 版本范围
version_name = "Velocity 1.8.x-1.21.11"

# 自定义名称
version_name = "Premium Network v1.0"
```

参考 `config.toml.example` 文件查看更多示例。

## 测试

运行 Velocity 模块的测试：

```bash
gradlew.bat :velocity:test
```

查看测试报告：`velocity/build/reports/tests/test/index.html`

查看覆盖率报告：`velocity/build/reports/jacoco/test/html/index.html`

## 注意事项

- 需要 Java 21 或更高版本
- 支持 Velocity 3.5 及更高版本
- 修改配置后需要重启服务器
- 仅修改版本信息，不影响 MOTD 和玩家列表
