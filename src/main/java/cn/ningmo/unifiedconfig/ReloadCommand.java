package cn.ningmo.unifiedconfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ReloadCommand implements CommandExecutor {
    private final UnifiedConfig plugin;
    
    public ReloadCommand(UnifiedConfig plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 控制台直接允许执行
        if (sender instanceof ConsoleCommandSender) {
            plugin.reloadServerConfig();
            String message = plugin.getServerConfig().getString("messages.reload-success", "&a配置已重新加载!");
            sender.sendMessage(message.replace("&", "§"));
            return true;
        }
        
        // 玩家需要权限检查
        if (!sender.hasPermission("unifiedconfig.reload")) {
            String message = plugin.getServerConfig().getString("messages.reload-no-permission", "&c你没有权限执行此命令!");
            sender.sendMessage(message.replace("&", "§"));
            return true;
        }
        
        plugin.reloadServerConfig();
        String message = plugin.getServerConfig().getString("messages.reload-success", "&a配置已重新加载!");
        sender.sendMessage(message.replace("&", "§"));
        return true;
    }
} 