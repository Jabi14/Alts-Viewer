package mc.jabi14.Events;

import mc.jabi14.AltsViewer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class OnJoin implements Listener {
    private AltsViewer plugin;

    public OnJoin(AltsViewer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getAltsManager().addUser(player);
        String ip = player.getAddress().getAddress().getHostAddress();
        String ipResult = plugin.getAltsManager().formatIP(ip);
        List<String> alts = plugin.getAltsManager().getAlts(ipResult, true);
        if(alts.size() > 1 && plugin.getMainConfigManager().getConfigFile().getConfig().getBoolean("config.join-message")) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.isOp()) p.sendMessage(AltsViewer.prefix +player.getName()+" has alts. Check /alts view "+player.getName());
            }
        }
    }
}