package mc.jabi14;

import mc.jabi14.config.MainConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AltsManager{
    private AltsViewer plugin;
    private List<AltsList> ips = new ArrayList<>();
    private FileConfiguration config;
    private FileConfiguration players;

    public AltsManager(AltsViewer plugin, MainConfigManager config, MainConfigManager players) {
        this.plugin = plugin;
        this.config = config.getConfigFile().getConfig();
        this.players = players.getConfigFile().getConfig();
    }

    public void addUser(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        int indice2 = ip.lastIndexOf(".");
        String resultado = (indice2 != -1) ? ip.substring(0, indice2) : ip;
        resultado += ".xx";
        boolean existeEnLista = false;
        loadInfo(resultado);
        AltsList altsList = null;
        for (AltsList a : this.ips) {
            if (a.getIp().equals(resultado)) {
                existeEnLista = true;
                altsList = a;
                break;
            }
        }
        if (!existeEnLista) {
            altsList = new AltsList(resultado,player.getName());
            this.ips.add(altsList);
            List<String> nicks = new ArrayList<>();
            nicks.add(player.getName());
            players.set("players." + resultado, nicks);
        }
        else{
            if (!altsList.getAccountList().contains(player.getName())) {
                altsList.addPlayer(player.getName());
                List<String> jugadores = players.getStringList("players." + resultado);
                jugadores.add(player.getName());
                players.set("players." + resultado, jugadores);
            }
            if (altsList.getAccountList().size() > (config.getInt("config.allowed-alts"))-1 && config.getBoolean("congig.enable-kick")) player.kickPlayer("a");
        }
        plugin.getPlayerConfigManager().getConfigFile().saveConfig();
    }

    private boolean searchPlayer(int x, int index, String nick){
        for (int i = x; i < this.ips.get(index).getSize(); i++) {
            String nick2 = this.ips.get(index).getAccountList().get(i);
            if(nick.equals(nick2)) return true;
        }
        return false;
    }

    private void loadInfo(String ip){
        for (int i = 0; i < players.getStringList("players").size(); i++) {
            for (String p: players.getStringList("players.")){

            }
        }
        List<String> Players = players.getStringList("players."+ip);
        if(players == null) return;
        AltsList altsList = new AltsList(ip);
        ips.add(altsList);
        for(String p:Players) altsList.addPlayer(p);
    }

    public List<String> getAlts(String nick){
        String ip = searchPlayer(nick);
        if(ip == null) return new ArrayList<>();
        for(AltsList IP:this.ips){
            if(IP.getIp().equals(ip)) return IP.getAccountList();
        }
        return null;
    }

    private String searchPlayer(String nick){
        for(AltsList IP:this.ips){
            for(String nicks:IP.getAccountList()){
                if(nicks.equals(nick)) return IP.getIp();
            }
        }
        return null;
    }
}