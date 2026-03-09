# VersionTinmoli

<p align="center">
  <img src="images/8823B3DC.jpg" alt="VersionTinmoli Banner" width="150">
</p>

> **Version**: 1.0.2  
> **Supported Platforms**: Velocity 3.0+ | BungeeCord 1.16+ | Bukkit/Spigot/Paper 1.20.1+

A lightweight Minecraft proxy/server plugin for customizing the version name displayed in the server list, with rich color and formatting support.

[中文文档](README_CN.md) | [English Documentation](README.md)

---

## Table of Contents

- [Features](#features)
- [Supported Platforms](#supported-platforms)
- [Quick Start](#quick-start)
- [Color Support](#color-support)
- [Commands](#commands)
- [Configuration](#configuration)
- [FAQ](#faq)
- [Changelog](#changelog)

---

## Features

- **Colored Version Names** - Velocity supports MiniMessage format (RGB, gradients, rainbow), BungeeCord/Bukkit support legacy color codes
- **Rich Formatting** - Bold, italic, underline, strikethrough support
- **Hot Reload** - Reload configuration with `/vt reload` command
- **Dynamic Modification** - Instantly modify version name with `/vt md <name>` command
- **Simple Configuration** - YAML configuration file
- **Lightweight** - No performance impact
- **Auto Fallback** - Compatible with older client versions
- **Multi-language** - Supports English and Chinese

---

## Supported Platforms

| Platform | Version | Java Version | Status |
|----------|---------|--------------|--------|
| Velocity | 3.0+ | Java 21+ | Fully Supported |
| BungeeCord | 1.16+ | Java 17+ | Fully Supported |
| Bukkit/Spigot | 1.20.1-1.21.11 | Java 17+ | Requires newer version |
| Paper | 1.20.1-1.21.11 | Java 17+ | Fully Supported |

---

## Quick Start

### Installation

1. Download the JAR file for your platform from [Releases](https://github.com/Tinmoli/VersionTinmoli/releases)
2. Place the JAR file in your server's `plugins/` directory
3. Restart the server
4. Edit the configuration file (auto-generated)
5. Use `/vt reload` to reload the configuration

### Configuration File Location

- **Velocity**: `plugins/versiontinmoli/config.yml`
- **BungeeCord**: `plugins/VersionTinmoli/config.yml`
- **Bukkit**: `plugins/VersionTinmoli/config.yml`

---

## Color Support

### Velocity - MiniMessage Format

```yaml
# Named colors
version_name: "<gold>Velocity</gold> <gray>1.8-1.21</gray>"

# RGB colors (1.16+)
version_name: "<#FF5555>Red <#55FF55>Green"

# Gradient effects
version_name: "<gradient:red:blue>Rainbow Server</gradient>"

# Rainbow effects
version_name: "<rainbow>Rainbow Server</rainbow>"

# Style combinations
version_name: "<bold><gold>VIP</gold></bold> <gray>| <green>Online</green></gray>"
```

**Available Colors**: `<red>`, `<gold>`, `<yellow>`, `<green>`, `<aqua>`, `<blue>`, `<light_purple>`, `<white>`, `<gray>`, `<dark_gray>`, `<black>`

**Available Styles**: `<bold>`, `<italic>`, `<underlined>`, `<strikethrough>`

---

### BungeeCord/Bukkit - Legacy Color Codes

```yaml
# Basic colors
version_name: "&6BungeeCord &71.8-1.21"

# With formatting
version_name: "&l&6VIP Server&r &7| &a1.8-1.21"

# Multi-color combinations
version_name: "&cRed &6Gold &eYellow &aGreen"
```

**Color Codes**: `&0`-`&9`, `&a`-`&f` (black, dark blue, dark green, dark aqua, dark red, purple, gold, gray, dark gray, blue, green, aqua, red, pink, yellow, white)

**Format Codes**: `&l` (bold), `&m` (strikethrough), `&n` (underline), `&o` (italic), `&r` (reset)

---

### Client Compatibility

| Client Version | Supported Colors |
|----------------|------------------|
| 1.16+ | Full RGB colors |
| 1.7-1.15 | 16 named colors |
| < 1.7 | Basic colors |

---

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/vt` | `versiontinmoli.admin` | Show plugin information |
| `/vt reload` | `versiontinmoli.admin` | Reload configuration file |
| `/vt md <name>` | `versiontinmoli.admin` | Modify version name |

**Permission Notes**: Velocity/BungeeCord require `versiontinmoli.admin` permission, Bukkit defaults to OP

---

## Configuration

### Basic Configuration

```yaml
# VersionTinmoli Configuration File
# Customize the version name displayed in the server list

# Language setting (en_US or zh_CN)
language: "en_US"

# Version name configuration
version_name: "Velocity 1.8.x-1.21.11"
```

### Language Settings

The plugin supports multiple languages. You can set the language in the configuration file:

- `en_US` - English
- `zh_CN` - Simplified Chinese

After modifying the `language` field, use `/vt reload` to reload the configuration and switch languages. All command messages and prompts will be displayed in the corresponding language.

### Language File Customization

Language files are automatically copied to the external directory on first run:
- **Velocity**: `plugins/versiontinmoli/lang/`
- **BungeeCord**: `plugins/VersionTinmoli/lang/`
- **Bukkit**: `plugins/VersionTinmoli/lang/`

You can edit these files to customize messages. The plugin will load from the external directory first, then fall back to the built-in files.

### Configuration Examples

**Regular Server**
```yaml
# Velocity
version_name: "<gold>Minecraft</gold> <gray>1.8-1.21</gray>"

# BungeeCord/Bukkit
version_name: "&6Minecraft &71.8-1.21"
```

**VIP Server**
```yaml
# Velocity
version_name: "<bold><gradient:gold:yellow>VIP Server</gradient></bold>"

# BungeeCord/Bukkit
version_name: "&l&6VIP Server"
```

**Minigames Server**
```yaml
# Velocity
version_name: "<rainbow>MiniGames</rainbow> <white>| <green>Online</green></white>"

# BungeeCord/Bukkit
version_name: "&cM&6i&en&ai&9G&ba&dm&5e&cs &f| &aOnline"
```

**PVP Server**
```yaml
# Velocity
version_name: "<bold><red>PVP</red></bold> <gray>| <yellow>Fighting</yellow></gray>"

# BungeeCord/Bukkit
version_name: "&l&cPVP &r&7| &eFighting"
```

---

## FAQ

### Migrating from TOML to YAML

If you're upgrading from an older version (before v1.0.2), you need to migrate your configuration from TOML format to YAML format.

**Automatic Migration (Recommended):**
1. Backup your `config.toml` file
2. Delete the `config.toml` file
3. Restart the server or run `/vt reload`
4. The plugin will automatically create a new `config.yml` file
5. Edit `config.yml` and set your custom version name

**Manual Migration:**

TOML format (old):
```toml
version_name = "My Server 1.20"
```

YAML format (new):
```yaml
version_name: "My Server 1.20"
```

Main difference: Use colon `:` instead of equals sign `=`

### Configuration Not Working

1. Ensure the configuration file is in the correct location
2. Check YAML syntax is correct
3. Use `/vt reload` to reload configuration
4. If still not working, restart the server

### Colors Not Displaying or Showing as Text

**Problem**: RGB color codes display as text like `Red</> Green</>`

**Cause**: Using incorrect closing tags `</>`

**Solution**:
```yaml
# Wrong
version_name: "<#FF5555>Red</> <#55FF55>Green</>"

# Correct
version_name: "<#FF5555>Red <#55FF55>Green"
version_name: "<color:#FF5555>Red</color> <color:#55FF55>Green</color>"
```

**Other Color Issues**:
1. **Velocity**: Use MiniMessage format `<gold>text</gold>`
2. **BungeeCord/Bukkit**: Use `&` or `§` color codes
3. RGB colors require Minecraft 1.16+ client
4. Check server logs for errors

### Platform-Specific Requirements

1. **Velocity**: Requires Velocity 3.0+ and Java 21+
2. **BungeeCord**: Requires BungeeCord 1.16+ and Java 17+
3. **Bukkit**: Requires Minecraft 1.20.1+ and Java 17+

---

## Changelog

### v1.0.2 (Latest)
- Migrated from TOML to YAML configuration format
- Added multi-language support (English and Chinese)
- Added external language file loading support
- Improved configuration file comments (English only)
- Fixed configuration reload issues
- Enhanced error handling and logging

### v1.0.1
- Initial release with TOML configuration
- Basic version name customization
- Color and formatting support

---

## License

This project is licensed under the MIT License.

## Support

For issues or suggestions, please submit them on [GitHub Issues](https://github.com/Tinmoli/VersionTinmoli/issues).

---

**Documentation Version**: 1.0.2  
**Maintainer**: Tinmoli



