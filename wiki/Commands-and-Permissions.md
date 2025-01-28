# 命令与权限

## 命令

### /uc
- 功能：重新加载配置文件
- 权限：unifiedconfig.reload
- 使用者：
  - 控制台：直接使用
  - 玩家：需要权限
- 使用场景：
  - 修改 serverconfig.yml 后应用更改
  - 需要重新写入插件配置时

## 权限

### unifiedconfig.reload
- 描述：允许使用 `/uc` 命令重新加载配置
- 默认值：op
- 建议授予：
  - 服务器管理员
  - 配置管理员

## 权限配置示例

### LuckPerms
```yaml
/lp user <用户名> permission set unifiedconfig.reload true
```

### OP
```yaml
/op <用户名>
```

## 注意事项

1. 控制台始终拥有执行命令的权限
2. 建议仅给信任的管理员权限
3. 可以通过权限插件精细控制权限 