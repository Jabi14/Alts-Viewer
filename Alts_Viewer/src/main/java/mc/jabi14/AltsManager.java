package mc.jabi14;

import mc.jabi14.config.MainConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //Cuando entra en el else, se pone dos veces el nick
    public void addUser(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        int i = ip.lastIndexOf(".");
        String ipResult = (i != -1) ? ip.substring(0, i) : ip;
        ipResult += ".xx";
        loadInfo(ipResult);
        if (!ipList.containsKey(ipResult)){
            ipList.put(ipResult,new ArrayList<>());
            ipList.get(ipResult).add(player.getName());
            players.set("players." + ipResult, ipList.get(ipResult));
        }
        else{
            if(!ipList.get(ipResult).contains(player.getName())){
                ipList.get(ipResult).add(player.getName());
                List<String> jugadores = players.getStringList("players." + ipResult);
                jugadores.add(player.getName());
                players.set("players." + ipResult, jugadores);
            }
            // añadir un and mas para checkear si es un alt o no
            if (ipList.get(ipResult).size() > config.getInt("config.allowed-alts")+1 && config.getBoolean("config.enable-kick")) player.kickPlayer("a");
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
    //No funciona
    private String loadInfoByNick(String nick){
        for(String i: players.getStringList("players")){
            for(String n: players.getStringList("players."+i)){
                if (n.equals(nick)) return i;
            }
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
        int i = ip.lastIndexOf(".");
        String ipResult = (i != -1) ? ip.substring(0, i) : ip;
        ipResult += ".xx";
        loadInfo(ip);
        return ipList.getOrDefault(ip, null);
    }
}