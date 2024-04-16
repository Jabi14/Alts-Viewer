package mc.jabi14.config;

import mc.jabi14.AltsViewer;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfigManager {
    private CustomConfig configFile;
    private String config;
    private AltsViewer plugin;
    private String kickMessage;

    public MainConfigManager(AltsViewer plugin, String config){
        this.plugin = plugin;
        configFile = new CustomConfig(config, null,plugin);
        configFile.registerConfig();
        loadConfig();
    }

    public MainConfigManager(AltsViewer plugin){
        this.plugin = plugin;
        configFile = new CustomConfig("config.yml", null,plugin);
        configFile.registerConfig();
        loadConfig();
    }

    public void loadConfig(){
        FileConfiguration config = configFile.getConfig();
        kickMessage = config.getString("messages.kick");
    }

    public void reloadConfig(){
        this.configFile.reloadConfig();
        loadConfig();
    }

    public CustomConfig getConfigFile() {
        return configFile;
    }
}
