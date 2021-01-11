package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_BroadCast implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if( p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.BroadCast.Send")) ) {
                StringBuilder msg = new StringBuilder();
                for (String m : args){
                    msg.append(" ").append(m);
                }


                Bukkit.broadcastMessage(Main.getPrefix() + "§7" + msg);

            }else{
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.BroadCast.Send")));
            }
        }
        return false;
    }
}
