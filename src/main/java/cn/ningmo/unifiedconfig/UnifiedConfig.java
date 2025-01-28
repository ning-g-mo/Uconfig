package cn.ningmo.unifiedconfig;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.io.IOException;

public class UnifiedConfig extends JavaPlugin {
    private File serverConfigFile;
    private FileConfiguration serverConfig;
    
    @Override
    public void onEnable() {
        // 创建配置文件
        createServerConfig();
        
        // 注册命令
        getCommand("uc").setExecutor(new ReloadCommand(this));
        
        // 检查更新 - 修改配置路径
        if (serverConfig.getBoolean("settings.check-update", true)) {
            new UpdateChecker(this).checkUpdate();
        }
        
        // 写入配置
        writeConfigs();
    }
    
    private void createServerConfig() {
        // 修改为服务器根目录
        serverConfigFile = new File(getServer().getWorldContainer(), "serverconfig.yml");
        if (!serverConfigFile.exists()) {
            // 从插件资源复制到服务器根目录
            try {
                saveResource("serverconfig.yml", true);
                File source = new File(getDataFolder(), "serverconfig.yml");
                java.nio.file.Files.move(
                    source.toPath(),
                    serverConfigFile.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                // 删除插件目录下的配置文件
                source.delete();
            } catch (IOException e) {
                getLogger().severe("无法创建配置文件: " + e.getMessage());
            }
        }
        serverConfig = YamlConfiguration.loadConfiguration(serverConfigFile);
    }
    
    public void writeConfigs() {
        if (!serverConfig.contains("plugins")) {
            return;
        }
        
        boolean verboseLogging = serverConfig.getBoolean("settings.verbose-logging", true);
        boolean backupEnabled = serverConfig.getBoolean("settings.backup-before-write", true);
        
        for (String pluginName : serverConfig.getConfigurationSection("plugins").getKeys(false)) {
            File pluginFolder = new File(getServer().getWorldContainer(), "plugins/" + pluginName);
            if (!pluginFolder.exists()) {
                sendMessage("plugin-not-found", new String[]{"%plugin%"}, new String[]{pluginName});
                continue;
            }
            
            // 获取该插件的配置信息
            for (String fileName : serverConfig.getConfigurationSection("plugins." + pluginName).getKeys(false)) {
                File configFile = new File(pluginFolder, fileName);
                
                // 在修改前创建备份
                if (backupEnabled && configFile.exists()) {
                    backupConfig(configFile);
                }
                
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                
                // 写入配置
                String path = "plugins." + pluginName + "." + fileName;
                for (String key : serverConfig.getConfigurationSection(path).getKeys(true)) {
                    if (!serverConfig.isConfigurationSection(path + "." + key)) {
                        config.set(key, serverConfig.get(path + "." + key));
                        if (verboseLogging) {
                            getLogger().info("设置 " + pluginName + "/" + fileName + ": " + key + " = " + serverConfig.get(path + "." + key));
                        }
                    }
                }
                
                try {
                    config.save(configFile);
                    sendMessage("write-success", 
                        new String[]{"%plugin%", "%file%"}, 
                        new String[]{pluginName, fileName});
                } catch (IOException e) {
                    sendMessage("write-failed",
                        new String[]{"%plugin%", "%file%", "%error%"},
                        new String[]{pluginName, fileName, e.getMessage()});
                }
            }
        }
    }
    
    private void sendMessage(String key, String[] placeholders, String[] values) {
        String message = serverConfig.getString("messages." + key, key);
        message = message.replace("&", "§");
        
        if (placeholders != null && values != null) {
            for (int i = 0; i < placeholders.length; i++) {
                message = message.replace(placeholders[i], values[i]);
            }
        }
        
        getLogger().info(message);
    }
    
    private void backupConfig(File configFile) {
        if (!serverConfig.getBoolean("settings.backup-before-write", true)) {
            return;
        }
        
        File backupFolder = new File(getDataFolder(), "backups");
        if (!backupFolder.exists()) {
            backupFolder.mkdirs();
        }
        
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        File backupFile = new File(backupFolder, configFile.getName() + "." + timestamp + ".bak");
        
        try {
            java.nio.file.Files.copy(configFile.toPath(), backupFile.toPath());
            sendMessage("backup-created", new String[]{"%file%"}, new String[]{backupFile.getName()});
        } catch (IOException e) {
            sendMessage("backup-failed", new String[]{"%error%"}, new String[]{e.getMessage()});
        }
    }
    
    public void reloadServerConfig() {
        // 重新从文件加载配置，这会读取最新的文件内容
        serverConfig = YamlConfiguration.loadConfiguration(serverConfigFile);
        // 应用新的配置
        writeConfigs();
    }
    
    private void setupMetrics() {
        // 移除此方法或保留为空
    }
    
    // 添加 getter 方法
    public FileConfiguration getServerConfig() {
        return serverConfig;
    }
} 