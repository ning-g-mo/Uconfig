# 配置文件说明

serverconfig.yml 位于服务器根目录，用于统一管理其他插件的配置。

## 文件结构

```yaml
# 基础设置
settings:
  # 是否检查更新
  check-update: true
  # 是否在修改配置前创建备份
  backup-before-write: true
  # 是否在写入配置时显示详细日志
  verbose-logging: true

# 插件配置写入列表
plugins:
  # 插件文件夹名称
  authme:
    # 重载命令
    reload-command: "authme reload"
    # 配置文件名称
    config.yml:
      # 配置路径和值
      settings:
        serverName: 我的服务器
      restrictions:
        maxRegPerIp: 3
        minPasswordLength: 6

# 消息设置
messages:
  reload-success: "&a配置已重新加载!"
  reload-no-permission: "&c你没有权限执行此命令!"
  backup-created: "&a已创建配置文件备份: &e%file%"
  backup-failed: "&c创建配置文件备份失败: &e%error%"
  write-success: "&a成功写入 &e%plugin%/%file%"
  write-failed: "&c无法保存 &e%plugin%/%file%&c: &e%error%"
  plugin-not-found: "&c插件文件夹 &e%plugin% &c不存在!"
```

## 配置说明

### 基础设置 (settings)
- `check-update`: 是否自动检查更新
- `backup-before-write`: 是否在修改配置前创建备份
- `verbose-logging`: 是否显示详细日志

### 插件配置 (plugins)
- 使用插件的文件夹名称作为键（区分大小写）
- 支持配置插件目录下的任意 yml 文件
- 配置路径需要与目标文件的路径完全匹配
- 可以通过 reload-command 指定插件的重载命令

### 消息设置 (messages)
- 支持颜色代码（使用 & 符号）
- 支持变量替换：
  - %file% - 文件名
  - %plugin% - 插件名
  - %error% - 错误信息

## 示例说明

1. 配置 AuthMe 插件：
```yaml
plugins:
  authme:
    config.yml:
      settings:
        serverName: 我的服务器
```
这会修改 plugins/authme/config.yml 中的 settings.serverName 值

2. 配置多个插件：
```yaml
plugins:
  authme:
    config.yml:
      settings:
        serverName: 服务器
  luckperms:
    config.yml:
      server:
        name: survival
```

## 注意事项

1. 插件名称必须与插件文件夹名称完全匹配
2. 配置路径必须与目标文件中的路径完全匹配
3. 修改配置后需要使用 `/uc` 命令重新加载
4. 建议开启 verbose-logging 以便排查问题