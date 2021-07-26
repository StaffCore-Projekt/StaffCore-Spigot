package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatClearCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public ChatClearCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("chatclear").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("Chat.Clear"))) {
                int a = 200;
                if (args.length == 0) {

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            for (int i = 0; i < a; i++) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (!all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Chat.Bypass"))) {
                                        all.sendMessage("\n");
                                    }
                                }
                            }

                            for (Player s : Bukkit.getOnlinePlayers()) {
                                s.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.ChatClear.Player")
                                        .replace("%player%", p.getName()));
                            }
                        }
                    }.runTaskAsynchronously(staffCore);

                } else {
                    String reason = "";
                    for (String s : args) {
                        reason = reason + " " + s;
                    }
                    reason = ChatColor.translateAlternateColorCodes('&', reason);
                    String finalReason = reason;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < a; i++) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (!all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Chat.Bypass"))) {
                                        all.sendMessage("\n");
                                    }
                                }
                            }

                            for (Player s : Bukkit.getOnlinePlayers()) {
                                s.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.ChatClear.With-Reason")
                                        .replace("%reason%", finalReason).replace("%player%", p.getName()));
                            }

                        }
                    }.runTaskAsynchronously(staffCore);

                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Chat.Clear")));
            }
        } else {
            int a = 200;
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i < a; i++) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.sendMessage("\n");
                        }
                    }

                    for (Player s : Bukkit.getOnlinePlayers()) {
                        s.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.ChatClear.Console"));
                        //Bukkit.broadcastMessage("Chat cleared by: Console!");
                    }
                }
            }.runTaskAsynchronously(staffCore);
        }
        return false;
    }
}
