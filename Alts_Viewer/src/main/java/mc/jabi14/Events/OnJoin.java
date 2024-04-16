package mc.jabi14.Events;

import mc.jabi14.AltsViewer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {
    private AltsViewer plugin;

    public OnJoin(AltsViewer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        plugin.getAltsManager().addUser(player);
    }
}