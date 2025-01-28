# 快速开始

## 安装

1. 从 [Releases](https://github.com/ning-g-mo/Uconfig/releases/latest) 下载最新版本
2. 将插件放入服务器的 plugins 文件夹
3. 启动服务器，插件会在根目录生成 serverconfig.yml

## 基础配置

1. 打开根目录下的 serverconfig.yml
2. 修改基础设置（可选）：
```yaml
settings:
  check-update: true        # 是否检查更新
  backup-before-write: true # 是否备份配置
  verbose-logging: true     # 是否显示详细日志
```

3. 添加需要管理的插件配置：
```yaml
plugins:
  插件文件夹名称:
    配置文件名称:
      配置项: 值
```

## 使用方法

1. 编辑 serverconfig.yml 添加或修改配置
2. 使用 `/uc` 命令重新加载配置（需要权限：unifiedconfig.reload）
3. 检查控制台输出确认配置是否成功写入

## 常见问题

- 如果配置未生效，请检查：
  - 插件文件夹名称是否正确（区分大小写）
  - 配置文件名称是否正确
  - 配置路径是否与目标文件完全匹配
  
- 建议开启 verbose-logging 以便排查问题

## 下一步

- 阅读 [配置文件说明](Configuration) 了解详细配置
- 查看 [常见问题](FAQ) 获取更多帮助