package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatFilterCommand implements CommandExecutor {
    private final StaffCore staffCore;

    public ChatFilterCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("chatfilter").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Chatfilter.Manage")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                if (args.length == 0) {
                    staffCore.getStaffCoreLoader().getInventoryHandler().openChatFilterSettings(p);
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.RED + "Too many arguments");
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Chatfilter.Manage")));
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.RED + ChatColor.BOLD
                            + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Console-Input"
                            + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }
        return true;
    }

}
