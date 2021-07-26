package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.utils.PanelManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCoreCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public StaffCoreCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("staffcore").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("gui")) {
                    if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                            .hasPermission(staffCore.getStaffCoreLoader().getPermission("System.Adminpanel"))) {
                        PanelManager manager = staffCore.getStaffCoreLoader().getPanelManager();

                        manager.openMainMenu(p);
                    } else {
                        if (staffCore.getStaffCoreLoader().getSystemManager().isExperimental()) {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v"
                                            + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                            + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                        } else if (staffCore.getStaffCoreLoader().getSystemManager().isLatest()) {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v"
                                            + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                            + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ChatColor.DARK_GRAY
                                            + ")");
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v"
                                            + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                            + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/staffcore gui " + ChatColor.DARK_GRAY + "| "
                                            + ChatColor.GRAY + "To open the adminconsole");
                        }
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "Created by: " + ChatColor.GRAY
                                + "LacoDev and ViaEnder");
                    }
                } else {
                    if (staffCore.getStaffCoreLoader().getSystemManager().isExperimental()) {
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                        + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                    } else if (staffCore.getStaffCoreLoader().getSystemManager().isLatest()) {
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                        + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                    } else {
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                        + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/staffcore gui " + ChatColor.DARK_GRAY + "| "
                                        + ChatColor.GRAY + "To open the adminconsole");
                    }
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "Created by: " + ChatColor.GRAY
                            + "LacoDev and ViaEnder");
                }
            } else {
                p.sendMessage("");
                if (staffCore.getStaffCoreLoader().getSystemManager().isExperimental()) {
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                } else if (staffCore.getStaffCoreLoader().getSystemManager().isLatest()) {
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                } else {
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/staffcore gui " + ChatColor.DARK_GRAY + "| "
                                    + ChatColor.GRAY + "To open the adminconsole");
                }
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "Created by: " + ChatColor.GRAY
                        + "LacoDev and ViaEnder");
                p.sendMessage("");
            }

        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {

                    staffCore.getStaffCoreLoader().getSystemManager().reloadStaffCore(sender);

                } else if (args[0].equalsIgnoreCase("update")) {

                    if (!staffCore.getStaffCoreLoader().getSystemManager().isLatest()) {
                        if (!staffCore.getStaffCoreLoader().getSystemManager().isExperimental()) {
                            staffCore.getStaffCoreLoader().getSystemManager().downloadLatestVersion(sender);
                        } else {
                            sender.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Version.Experimental"));
                        }
                    } else {
                        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Version.Latest"));
                    }
                }
            } else {
                sender.sendMessage("");
                if (staffCore.getStaffCoreLoader().getSystemManager().isExperimental()) {
                    sender.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                } else if (staffCore.getStaffCoreLoader().getSystemManager().isLatest()) {
                    sender.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                } else {
                    sender.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                    sender.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/staffcore gui " + ChatColor.DARK_GRAY + "| "
                                    + ChatColor.GRAY + "To open the adminconsole");
                }
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "Created by: " + ChatColor.GRAY
                        + "LacoDev and ViaEnder");
                sender.sendMessage("");
            }
        }

        return true;
    }

}
