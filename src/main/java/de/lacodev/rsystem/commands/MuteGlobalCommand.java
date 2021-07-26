package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteGlobalCommand implements CommandExecutor {
    private final StaffCore staffCore;

    public MuteGlobalCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("muteglobal").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("Mute.Global.Change"))) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("on")) {
                        if (staffCore.getStaffCoreLoader().getBanManager().isGMute()) {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Already-Disabled"));
                        } else {
                            staffCore.getStaffCoreLoader().getBanManager().setGlobalMute(true);
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Disable"));
                            Bukkit.broadcastMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Start-Mute")
                                            .replace("%player%", p.getName()));
                        }
                    } else if (args[0].equalsIgnoreCase("off")) {
                        if (staffCore.getStaffCoreLoader().getBanManager().isGMute()) {
                            staffCore.getStaffCoreLoader().getBanManager().setGlobalMute(false);
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Enable"));
                            Bukkit.broadcastMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Stop-Mute")
                                            .replace("%player%", p.getName()));
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Already-Enabled"));
                        }
                    } else if (args[0].equalsIgnoreCase("info")) {
                        if (staffCore.getStaffCoreLoader().getBanManager().isGMute()) {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.IsDisabled"));
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.IsEnabled"));
                        }
                    } else {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Usage"));
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Usage"));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Mute.Global.Change")));
            }
        }
        return false;
    }
}
