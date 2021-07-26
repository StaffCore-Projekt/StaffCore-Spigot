package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaintenanceCommand implements CommandExecutor {

    private final String key = "maintenance";
    private final StaffCore staffCore;

    public MaintenanceCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("maintenance").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length != 1) {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Maintenance.Change")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {

                    if (staffCore.getStaffCoreLoader().getSettingsManager().isKey(key)) {
                        if (staffCore.getStaffCoreLoader().getSettingsManager().getBoolean(key)) {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Enabled!");
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Disabled!");
                        }
                    } else {
                        setState(false);
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Disabled!");
                    }

                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Maintenance.Change")));
                }
            } else {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Maintenance.Change")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {

                    switch (args[0].toLowerCase()) {
                        case "on":
                            if (setState(true)) {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is now Enabled!");
                                for (Player target : Bukkit.getOnlinePlayers()) {
                                    if (!(
                                            target.hasPermission(staffCore.getStaffCoreLoader().getPermission("Maintenance.Join"))
                                                    || target
                                                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")))) {
                                        target
                                                .kickPlayer(ChatColor.RED + "This Server is switched to Maintenance Mode!");
                                    }
                                }
                            } else {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is already Enabled!");
                            }
                            break;
                        case "off":
                            if (setState(false)) {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is now Disabled!");
                            } else {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is already Disabled!");
                            }
                            break;
                        case "info":
                            if (staffCore.getStaffCoreLoader().getSettingsManager().isKey(key)) {
                                if (staffCore.getStaffCoreLoader().getSettingsManager().getBoolean(key)) {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Enabled!");
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Disabled!");
                                }
                            } else {
                                setState(false);
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Disabled!");
                            }

                            break;
                        default:
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "TRY: /maintenance [ on | off | info ]");
                            break;
                    }

                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Maintenance.Change")));
                }
            }
        } else {
            if (args.length != 1) {

                if (staffCore.getStaffCoreLoader().getSettingsManager().isKey(key)) {
                    if (staffCore.getStaffCoreLoader().getSettingsManager().getBoolean(key)) {
                        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Enabled!");
                    } else {
                        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Disabled!");
                    }
                } else {
                    setState(false);
                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Disabled!");
                }

            } else {

                switch (args[0].toLowerCase()) {
                    case "on":
                        if (setState(true)) {
                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is now Enabled!");
                            for (Player target : Bukkit.getOnlinePlayers()) {
                                if (!(
                                        target.hasPermission(staffCore.getStaffCoreLoader().getPermission("Maintenance.Join"))
                                                || target
                                                .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")))) {
                                    target
                                            .kickPlayer(ChatColor.RED + "This Server is switched to Maintenance Mode!");
                                }
                            }
                        } else {
                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is already Enabled!");
                        }
                        break;
                    case "off":
                        if (setState(false)) {
                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is now Disabled!");
                        } else {
                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is already Disabled!");
                        }
                        break;
                    case "info":
                        if (staffCore.getStaffCoreLoader().getSettingsManager().isKey(key)) {
                            if (staffCore.getStaffCoreLoader().getSettingsManager().getBoolean(key)) {
                                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Enabled!");
                            } else {
                                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Disabled!");
                            }
                        } else {
                            setState(false);
                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "The Maintenance Mode is Disabled!");
                        }

                        break;
                    default:
                        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "TRY: /maintenance [ on | off | info ]");
                        break;
                }
            }
        }
        return false;
    }

    public boolean setState(boolean value) {
        if (value) {
            if (staffCore.getStaffCoreLoader().getSettingsManager().isKey(key)) {
                if (staffCore.getStaffCoreLoader().getSettingsManager().getBoolean(key)) {
                    //bereits an!
                    return false;
                } else {
                    staffCore.getStaffCoreLoader().getSettingsManager().update(key, true);
                    return true;
                }
            } else {
                staffCore.getStaffCoreLoader().getSettingsManager().set(key, true);
                return true;
            }
        } else {
            if (staffCore.getStaffCoreLoader().getSettingsManager().isKey(key)) {
                if (!staffCore.getStaffCoreLoader().getSettingsManager().getBoolean(key)) {
                    //bereits aus!
                    return false;
                } else {
                    staffCore.getStaffCoreLoader().getSettingsManager().update(key, false);
                    return true;
                }
            } else {
                staffCore.getStaffCoreLoader().getSettingsManager().set(key, true);
                return true;
            }
        }
    }
}
