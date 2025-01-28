package cn.ningmo.unifiedconfig;

import org.bukkit.Bukkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
    private final UnifiedConfig plugin;
    private final String GITHUB_API_URL = "https://api.github.com/repos/ning-g-mo/Uconfig/releases/latest";
    
    public UpdateChecker(UnifiedConfig plugin) {
        this.plugin = plugin;
    }
    
    public void checkUpdate() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(GITHUB_API_URL);
                InputStream inputStream = url.openStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder builder = new StringBuilder();
                
                while(scanner.hasNextLine()) {
                    builder.append(scanner.nextLine());
                }
                
                String response = builder.toString();
                
                // 使用正则表达式提取版本号
                String latestVersion = response.replaceAll(".*\"tag_name\":\"v([0-9.]+)\".*", "$1");
                String currentVersion = plugin.getDescription().getVersion();
                
                if (compareVersions(currentVersion, latestVersion) < 0) {
                    plugin.getLogger().info("发现新版本: v" + latestVersion);
                    plugin.getLogger().info("当前版本: v" + currentVersion);
                    plugin.getLogger().info("请访问 https://github.com/ningmo/unifiedconfig/releases 下载最新版本");
                }
                
                scanner.close();
                inputStream.close();
            } catch (IOException e) {
                plugin.getLogger().warning("检查更新失败: " + e.getMessage());
            }
        });
    }
    
    private int compareVersions(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");
        
        int length = Math.max(v1Parts.length, v2Parts.length);
        for (int i = 0; i < length; i++) {
            int v1 = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2 = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
            
            if (v1 < v2) return -1;
            if (v1 > v2) return 1;
        }
        return 0;
    }
} 