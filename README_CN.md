# VersionTinmoli

![VersionTinmoli Banner](images/banner.png)

> **版本**: 1.0.2  
> **支持平台**: Velocity 3.0+ | BungeeCord 1.16+ | Bukkit/Spigot/Paper 1.20.1+

一个轻量级的 Minecraft 代理/服务器插件，用于自定义服务器列表中显示的版本名称，支持丰富的颜色和格式。

[中文文档](README_CN.md) | [English Documentation](README.md)

---

## 目录

- [功能特性](#功能特性)
- [支持的平台](#支持的平台)
- [快速开始](#快速开始)
- [颜色支持](#颜色支持)
- [命令使用](#命令使用)
- [配置说明](#配置说明)
- [常见问题](#常见问题)
- [更新日志](#更新日志)

---

## 功能特性

- **彩色版本名称** - Velocity 支持 MiniMessage 格式（RGB、渐变、彩虹），BungeeCord/Bukkit 支持传统颜色代码
- **丰富的格式** - 支持粗体、斜体、下划线、删除线
- **热重载** - 使用 `/vt reload` 命令重载配置
- **动态修改** - 使用 `/vt md <名称>` 命令即时修改版本名称
- **简单配置** - 使用 YAML 配置文件
- **轻量级** - 无性能影响
- **自动降级** - 兼容旧版本客户端

---

## 支持的平台

| 平台 | 版本要求 | Java 版本 | 状态 |
|------|---------|----------|------|
| Velocity | 3.0+ | Java 21+ | 完全支持 |
| BungeeCord | 1.16+ | Java 17+ | 完全支持 |
| Bukkit/Spigot | 1.20.1-1.21.11 | Java 17+ | 需要较新版本 |
| Paper | 1.20.1-1.21.11 | Java 17+ | 完全支持 |

---

## 快速开始

### 安装步骤

1. 从 [Releases](https://github.com/Tinmoli/VersionTinmoli/releases) 下载对应平台的 JAR 文件
2. 将 JAR 文件放入服务器的 `plugins/` 目录
3. 重启服务器
4. 编辑配置文件（自动生成）
5. 使用 `/vt reload` 重载配置

### 配置文件位置

- **Velocity**: `plugins/versiontinmoli/config.yml`
- **BungeeCord**: `plugins/VersionTinmoli/config.yml`
- **Bukkit**: `plugins/VersionTinmoli/config.yml`

---

## 颜色支持

### Velocity - MiniMessage 格式

```yaml
# 命名颜色
version_name: "<gold>Velocity</gold> <gray>1.8-1.21</gray>"

# RGB 颜色 (1.16+)
version_name: "<#FF5555>Red <#55FF55>Green"

# 渐变效果
version_name: "<gradient:red:blue>Rainbow Server</gradient>"

# 彩虹效果
version_name: "<rainbow>Rainbow Server</rainbow>"

# 样式组合
version_name: "<bold><gold>VIP</gold></bold> <gray>| <green>Online</green></gray>"
```

**可用颜色**: `<red>`, `<gold>`, `<yellow>`, `<green>`, `<aqua>`, `<blue>`, `<light_purple>`, `<white>`, `<gray>`, `<dark_gray>`, `<black>`

**可用样式**: `<bold>`, `<italic>`, `<underlined>`, `<strikethrough>`

---

### BungeeCord/Bukkit - 传统颜色代码

```yaml
# 基础颜色
version_name: "&6BungeeCord &71.8-1.21"

# 带格式
version_name: "&l&6VIP Server&r &7| &a1.8-1.21"

# 多色组合
version_name: "&c红色 &6金色 &e黄色 &a绿色"
```

**颜色代码**: `&0`-`&9`, `&a`-`&f` (黑、深蓝、深绿、深青、深红、紫、金、灰、深灰、蓝、绿、青、红、粉、黄、白)

**格式代码**: `&l` (粗体), `&m` (删除线), `&n` (下划线), `&o` (斜体), `&r` (重置)

---

### 客户端兼容性

| 客户端版本 | 支持的颜色 |
|-----------|-----------|
| 1.16+ | 完整 RGB 颜色 |
| 1.7-1.15 | 16 种命名颜色 |
| < 1.7 | 基础颜色 |

---

## 命令使用

| 命令 | 权限 | 描述 |
|------|------|------|
| `/vt` | `versiontinmoli.admin` | 显示插件信息 |
| `/vt reload` | `versiontinmoli.admin` | 重载配置文件 |
| `/vt md <名称>` | `versiontinmoli.admin` | 修改版本名称 |

**权限说明**: Velocity/BungeeCord 需要 `versiontinmoli.admin` 权限，Bukkit 默认为 OP

---

## 配置说明

### 基础配置

```yaml
# VersionTinmoli Configuration File
# Customize the version name displayed in the server list

# Language setting (en_US or zh_CN)
language: "en_US"

# Version name configuration
version_name: "Velocity 1.8.x-1.21.11"
```

### 语言设置

插件支持多语言，可以在配置文件中设置：

- `en_US` - English (英文)
- `zh_CN` - 简体中文

修改 `language` 字段后，使用 `/vt reload` 重载配置即可切换语言。所有命令消息、提示信息都会使用对应的语言显示。

### 配置示例

**普通服务器**
```yaml
# Velocity
version_name: "<gold>Minecraft</gold> <gray>1.8-1.21</gray>"

# BungeeCord/Bukkit
version_name: "&6Minecraft &71.8-1.21"
```

**VIP 服务器**
```yaml
# Velocity
version_name: "<bold><gradient:gold:yellow>VIP Server</gradient></bold>"

# BungeeCord/Bukkit
version_name: "&l&6VIP Server"
```

**小游戏服务器**
```yaml
# Velocity
version_name: "<rainbow>MiniGames</rainbow> <white>| <green>Online</green></white>"

# BungeeCord/Bukkit
version_name: "&c小&6游&e戏 &f| &a在线"
```

**PVP 服务器**
```yaml
# Velocity
version_name: "<bold><red>PVP</red></bold> <gray>| <yellow>战斗中</yellow></gray>"

# BungeeCord/Bukkit
version_name: "&l&cPVP &r&7| &e战斗中"
```**Velocity**: 需要 Velocity 3.0+ 和 Java 21+
2. **BungeeCord**: 需要 BungeeCord 1.16+ 和 Java 17+
3. **Bukkit**: 需要 Minecraft 1.20.1+ 和 Java 17+
## 常见问题
## 常见问题

### 从 TOML 迁移到 YAML

如果您从旧版本（v1.0.2 之前）升级，需要将配置文件从 TOML 格式迁移到 YAML 格式。

**自动迁移（推荐）：**
1. 备份您的 `config.toml` 文件
2. 删除 `config.toml` 文件
3. 重启服务器或执行 `/vt reload`
4. 插件将自动创建新的 `config.yml` 文件
5. 编辑 `config.yml`，设置您的自定义版本名称

**手动迁移：**

TOML 格式（旧）：
```toml
version_name = "My Server 1.20"
```

YAML 格式（新）：
```yaml
version_name: "My Server 1.20"
```

主要区别：使用冒号 `:` 代替等号 `=`

### 配置文件未生效

1. 确保配置文件位于正确的位置
2. 检查 YAML 语法是否正确
3. 使用 `/vt reload` 重载配置
4. 如果还是不行，重启服务器
### 颜色不显示或显示为文本

**问题**: RGB 颜色代码显示为 `Red</> Green</>` 这样的文本

**原因**: 使用了错误的闭合标签 `</>`

**解决方案**:
```toml
# 错误写法
version_name = "<#FF5555>Red</> <#55FF55>Green</>"

# 正确写法
version_name = "<#FF5555>Red <#55FF55>Green"
version_name = "<color:#FF5555>Red</color> <color:#55FF55>Green</color>"
```

**其他颜色问题**:
1. **Velocity**: 使用 MiniMessage 格式 `<gold>文本</gold>`
2. **BungeeCord/Bukkit**: 使用 `&` 或 `§` 颜色代码
3. RGB 颜色需要 Minecraft 1.16+ 客户端
4. 查看服务器日志是否有错误


---

## 许可证

本项目采用 MIT 许可证。

## 支持

如有问题或建议，请在 [GitHub Issues](https://github.com/Tinmoli/VersionTinmoli/issues) 提交。

---

**文档版本**: 1.0.2  
**维护者**: Tinmoli
