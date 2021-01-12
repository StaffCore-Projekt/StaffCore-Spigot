package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_StaffChat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if( p.hasPermission(Main.getPermissionNotice("Permissions.StaffChat.Write")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) ) {
                StringBuilder message = new StringBuilder();
                for(String s : args){
                    message.append(" ").append(s);
                }

                for(Player all : Bukkit.getOnlinePlayers()){
                    if(all.hasPermission(Main.getPermissionNotice("Permissions.StaffChat.Read")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything")) ) {
                        all.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "StaffChat" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + p.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + message);
                    }
                }
            }else{
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.StaffChat.Write")));
            }
        }
        return false;
    }
}
