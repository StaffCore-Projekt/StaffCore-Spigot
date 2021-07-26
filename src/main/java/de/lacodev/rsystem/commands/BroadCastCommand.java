package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadCastCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public BroadCastCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("broadcast").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("BroadCast.Send"))) {
                String message = "";
                for (String s : args) {
                    message = message + " " + s;
                }

                message = ChatColor.translateAlternateColorCodes('&', "" + message);

                Bukkit.broadcastMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + message);

            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("BroadCast.Send")));
            }
        }
        return false;
    }
}
