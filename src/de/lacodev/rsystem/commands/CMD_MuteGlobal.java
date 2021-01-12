package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_MuteGlobal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if( p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Mute.Global.Change")) ) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("on")) {
                        if (BanManager.isGMute()) {
                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Already-Disabled"));
                        } else {
                            BanManager.setGlobalMute(true);
                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Disable"));
                            Bukkit.broadcastMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Start-Mute").replace("%player%", p.getName()));
                        }
                    } else if (args[0].equalsIgnoreCase("off")) {
                        if (BanManager.isGMute()) {
                            BanManager.setGlobalMute(false);
                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Enable"));
                            Bukkit.broadcastMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Stop-Mute").replace("%player%", p.getName()));
                        } else {
                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Already-Enabled"));
                        }
                    } else if (args[0].equalsIgnoreCase("info")) {
                        if (BanManager.isGMute()) {
                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.IsDisabled"));
                        } else {
                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.IsEnabled"));
                        }
                    } else {
                        p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Usage"));
                    }
                }else{
                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Usage"));
                }
            }else{
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Mute.Global.Change")));
            }
        }
        return false;
    }
}
