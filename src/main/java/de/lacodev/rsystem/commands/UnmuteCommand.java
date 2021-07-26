package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {
    private final StaffCore staffCore;

    public UnmuteCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("unmute").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("UnMute.Use"))) {
                    if (args.length == 1) {
                        if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                            if (staffCore.getStaffCoreLoader().getBanManager().isMuted(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                if (staffCore.getStaffCoreLoader().getBanManager().unmute(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                    staffCore.getStaffCoreLoader().getReportManager()
                                            .sendNotify("UNMUTE", p.getName(), staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]),
                                                    null);
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Success"));
                                } else {
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Error"));
                                }
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Not-Muted"));
                            }
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Cannot-find-player"));
                        }
                    } else {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Usage"));
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("UnMute.Use")));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }

        } else {
            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length == 1) {
                    if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                        if (staffCore.getStaffCoreLoader().getBanManager().isMuted(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                            if (staffCore.getStaffCoreLoader().getBanManager().unmute(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                staffCore.getStaffCoreLoader().getReportManager()
                                        .sendNotify("UNMUTE", "Console", staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]), null);
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Success"));
                            } else {
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Error"));
                            }
                        } else {
                            sender.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Not-Muted"));
                        }
                    } else {
                        sender.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Cannot-find-player"));
                    }
                } else {
                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.UnMute.Usage"));
                }
            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        }
        return true;
    }

}
