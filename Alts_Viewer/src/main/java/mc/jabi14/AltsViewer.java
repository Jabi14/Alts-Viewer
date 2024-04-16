package mc.jabi14;

import mc.jabi14.Events.OnJoin;
import mc.jabi14.commands.ComandoPrincipal;
import mc.jabi14.config.MainConfigManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AltsViewer extends JavaPlugin {
    private String version;
    private String nombre;
    public static String prefix = "&b[ALTS]&f ";
    private MainConfigManager mainConfigManager;
    private MainConfigManager playerConfigManager;
    private AltsManager altsManager;


    public void onEnable() {
        mainConfigManager = new MainConfigManager(this);
        playerConfigManager = new MainConfigManager(this,"players.yml");
        altsManager = new AltsManager(this, mainConfigManager, playerConfigManager);
        this.registrarComando();
        this.getServer().getPluginManager().registerEvents(new OnJoin(this), this);
    }

    public void registrarComando() {
        this.getCommand("alts").setExecutor(new ComandoPrincipal(this, altsManager));
    }

    public MainConfigManager getPlayerConfigManager() {
        return playerConfigManager;
    }

    public MainConfigManager getMainConfigManager() {
        return mainConfigManager;
    }

    public AltsManager getAltsManager() {
        return altsManager;
    }
}