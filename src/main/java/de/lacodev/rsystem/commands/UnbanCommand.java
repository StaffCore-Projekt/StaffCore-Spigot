package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbanCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public UnbanCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("unban").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("UnBan.Use"))) {
                    if (args.length == 1) {
                        if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                            if (staffCore.getStaffCoreLoader().getBanManager().isBanned(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                if (staffCore.getStaffCoreLoader().getBanManager().unban(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                    staffCore.getStaffCoreLoader().getReportManager().sendNotify("UNBAN", p.getName(), args[0], null);
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Success"));
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Error"));
                                }
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Not-Banned"));
                            }
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Cannot-find-player"));
                        }
                    } else {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Usage"));
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("UnBan.Use")));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }

        } else {
            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length == 1) {
                    if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                        if (staffCore.getStaffCoreLoader().getBanManager().isBanned(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                            if (staffCore.getStaffCoreLoader().getBanManager().unban(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                staffCore.getStaffCoreLoader().getReportManager().sendNotify("UNBAN", "Console", args[0], null);
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Success"));
                            } else {
                                sender
                                        .sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Error"));
                            }
                        } else {
                            sender.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Not-Banned"));
                        }
                    } else {
                        sender.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Cannot-find-player"));
                    }
                } else {
                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Usage"));
                }
            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        }
        return true;
    }

}
