package mc.jabi14;

import mc.jabi14.config.MainConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class AltsManager{
    private AltsViewer plugin;
    private Map<String,List<String>> ipList = new HashMap<>();
    private FileConfiguration config;
    private FileConfiguration players;

    public AltsManager(AltsViewer plugin, MainConfigManager config, MainConfigManager players) {
        this.plugin = plugin;
        this.config = config.getConfigFile().getConfig();
        this.players = players.getConfigFile().getConfig();
    }

    public String formatIP(String ip){
        int i = ip.lastIndexOf(".");
        String ipResult = (i != -1) ? ip.substring(0, i) : ip;
        return ipResult += ".xx";
    }

    public void addUser(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        String ipResult = formatIP(ip);
        loadInfo(ipResult);
        if (!ipList.containsKey(ipResult)){
            ipList.put(ipResult,new ArrayList<>());
            ipList.get(ipResult).add(player.getName());
            players.set("players." + ipResult, ipList.get(ipResult));
        }
        else{
            if(!ipList.get(ipResult).contains(player.getName())){
                ipList.get(ipResult).add(player.getName());
                players.set("players." + ipResult,  ipList.get(ipResult));
            }
            if (ipList.get(ipResult).size() > config.getInt("config.allowed-alts")+1 && config.getBoolean("config.enable-kick") && ipList.get(ipResult).indexOf(player.getName())+1 > config.getInt("config.allowed-alts")+1) player.kickPlayer(this.config.getString("messages.kick-message"));
        }
        plugin.getPlayerConfigManager().getConfigFile().saveConfig();
    }

    private void loadInfo(String ip){
        if(!ipList.containsKey(ip)){
            List<String> Players = players.getStringList("players."+ip);
            if(!Players.isEmpty()){
                ipList.put(ip,new ArrayList<>());
                for(String n: Players) ipList.get(ip).add(n);
            }
        }
    }

    private String loadInfoByNick(String nick){
        ConfigurationSection players = this.players.getConfigurationSection("players");
        for(Map.Entry<String, Object> entry : players.getValues(true).entrySet()){
            if(this.players.getStringList("players."+entry.getKey()).contains(nick)) return entry.getKey();
        }
        return null;
    }

    public List<String> getAlts(String msg, boolean searchMethod){
        if(searchMethod) return searchByIp(msg);
        else return searchByUser(msg);
    }

    private List<String> searchByUser(String nick){
        String ip = loadInfoByNick(nick);
        if(ip == null) return null;
        return searchByIp(ip);
    }

    private List<String> searchByIp(String ip){
        loadInfo(ip);
        return ipList.getOrDefault(ip, null);
    }
}