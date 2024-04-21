package mc.jabi14.commands;

import mc.jabi14.AltsManager;
import mc.jabi14.AltsViewer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ComandoPrincipal implements CommandExecutor {
    private AltsViewer plugin;
    private AltsManager altsManager;

    public ComandoPrincipal(AltsViewer plugin, AltsManager altsManager) {
        this.plugin = plugin;
        this.altsManager = altsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args){
        if(!sender.isOp()) return true;
        if(args.length >=3) helpMenu(sender);
        else if(args.length == 0) {
            sender.sendMessage(AltsViewer.prefix+"Use the command &b/alts help&f to see the commands.");
            return true;
        }
        else if(args[0].equalsIgnoreCase("help")){
            helpMenu(sender);
            return true;
        }
        else if (args[0].equalsIgnoreCase("reload")) {
            plugin.getMainConfigManager().reloadConfig();
        }
        else if (args[0].equalsIgnoreCase("view")) {
            if(args[1] == null) sender.sendMessage(AltsViewer.prefix+"Usage: &b/alts view <player>");
            else{
                List<String> alts = altsManager.getAlts(args[1],false);
                if(alts != null && alts.size() > 1) alts.remove(args[1]);
                else{
                    sender.sendMessage(AltsViewer.prefix+"No alts found");
                    return true;
                }
                sender.sendMessage(AltsViewer.prefix+"Found "+alts.size()+" alts:");
                for(String nicks:alts){
                    sender.sendMessage("- "+nicks);
                }
                alts.add(args[1]);
            }
            return true;
        }
        else if(args[0].equalsIgnoreCase("ipview")) {
            if(args[1] == null) sender.sendMessage(AltsViewer.prefix+"Usage: &b/alts view <player>");
            else{
                int i = args[1].lastIndexOf(".");
                String ipResult = (i != -1) ? (args[1].substring(0, i)) : (args[1]);
                ipResult += ".xx";
                List<String> alts = altsManager.getAlts(ipResult,true);
                if(alts == null){
                    sender.sendMessage(AltsViewer.prefix+"No alts found");
                    return true;
                }
                sender.sendMessage(AltsViewer.prefix+"Found "+alts.size()+" accounts:");
                for(String nicks:alts){
                    sender.sendMessage("- "+nicks);
                }
            }
            return true;
        }
        else{
            int i = 0;
            for (Player p : Bukkit.getOnlinePlayers()){
                if(p.getName().equals(args[0])){
                    i = 1;
                    sender.sendMessage(p.getName()+"`s info:");
                    sender.sendMessage("UUID: "+p.getUniqueId());
                    sender.sendMessage("Ip: "+p.getAddress().getAddress().getHostAddress());
                    sender.sendMessage("Gamemode: "+p.getGameMode());
                    sender.sendMessage("World: "+ p.getWorld().getName());
                    return true;
                }
            }
            if(i == 0) sender.sendMessage(AltsViewer.prefix+"Player offline or does not exist");
        }
        return true;
    }

    public void helpMenu(CommandSender sender){
        sender.sendMessage("&b<---------------- "+AltsViewer.prefix+" ---------------->");
        sender.sendMessage("&b/alts view <player> &fCheck player alts");
        sender.sendMessage("&b/alts ipview <ip> &fCheck accounts with that ip");
        sender.sendMessage("&b/alts <player> &f Check player info");
        sender.sendMessage("&b/alts reload &f reload config");
        sender.sendMessage("&b<-------------------------------->");
    }
}