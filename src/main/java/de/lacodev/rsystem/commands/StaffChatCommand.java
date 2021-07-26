package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public StaffChatCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("staffchat").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("StaffChat.Write")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                StringBuilder message = new StringBuilder();
                for (String s : args) {
                    message.append(" ").append(s);
                }

                message = new StringBuilder(ChatColor.translateAlternateColorCodes('&', "" + message));

                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("StaffChat.Read")) || all
                            .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                        all.sendMessage(
                                ChatColor.DARK_GRAY + "[" + ChatColor.RED + "StaffChat" + ChatColor.DARK_GRAY + "] "
                                        + ChatColor.GRAY + p.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY
                                        + message);
                    }
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("StaffChat.Write")));
            }
        }
        return false;
    }
}
