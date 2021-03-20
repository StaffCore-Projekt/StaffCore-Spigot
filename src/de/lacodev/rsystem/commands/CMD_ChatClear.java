package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CMD_ChatClear implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if( p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Chat.Clear")) ) {
                int a = 200;
                if (args.length == 0) {

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            for (int i = 0; i < a; i++) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (!all.hasPermission(Main.getPermissionNotice("Permissions.Chat.Bypass"))) {
                                        all.sendMessage("\n");
                                    }
                                }
                            }

                            for (Player s : Bukkit.getOnlinePlayers()) {
                                s.sendMessage(Main.prefix + Main.getMSG("Messages.System.ChatClear.Player").replace("%player%", p.getName()));
                            }
                        }
                    }.runTaskAsynchronously(Main.getInstance());

                }else{
                    String reason = "";
                    for (String s : args){
                        reason= reason + " " + s;
                    }
                    reason = ChatColor.translateAlternateColorCodes('&', reason);
                    String finalReason = reason;
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            for (int i = 0; i < a; i++){
                                for (Player all : Bukkit.getOnlinePlayers()){
                                    if (!all.hasPermission(Main.getPermissionNotice("Permissions.Chat.Bypass"))){
                                        all.sendMessage("\n");
                                    }
                                }
                            }

                            for (Player s : Bukkit.getOnlinePlayers()) {
                                s.sendMessage(Main.prefix + Main.getMSG("Messages.System.ChatClear.With-Reason").replace("%reason%", finalReason).replace("%player%", p.getName()));
                            }

                        }
                    }.runTaskAsynchronously(Main.getInstance());

                }
            } else {
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Chat.Clear")));
            }
        }else{
            int a = 200;
            new BukkitRunnable(){
                @Override
                public void run() {
                    for (int i = 0; i < a; i++) {
                        for (Player all : Bukkit.getOnlinePlayers()){
                            all.sendMessage("\n");
                        }
                    }

                    for (Player s : Bukkit.getOnlinePlayers()){
                        s.sendMessage(Main.prefix + Main.getMSG("Messages.System.ChatClear.Console"));
                        //Bukkit.broadcastMessage("Chat cleared by: Console!");
                    }
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
        return false;
    }
}
