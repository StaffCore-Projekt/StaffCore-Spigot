package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.PanelManager;
import de.lacodev.rsystem.utils.SystemManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_StaffCore implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("gui")) {
                    if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p
                            .hasPermission(Main.getPermissionNotice("Permissions.System.Adminpanel"))) {
                        PanelManager manager = new PanelManager();

                        manager.openMainMenu(p);
                    } else {
                        if (Main.getInstance().experimental == true) {
                            p.sendMessage(
                                    Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v"
                                            + Main.getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                            + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                        } else if (Main.getInstance().latest == true) {
                            p.sendMessage(
                                    Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v"
                                            + Main.getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                            + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ChatColor.DARK_GRAY
                                            + ")");
                        } else {
                            p.sendMessage(
                                    Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v"
                                            + Main.getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                            + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                            p.sendMessage(
                                    Main.getPrefix() + ChatColor.GRAY + "/staffcore gui " + ChatColor.DARK_GRAY + "| "
                                            + ChatColor.GRAY + "To open the adminconsole");
                        }
                        p.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "Created by: " + ChatColor.GRAY
                                + "LacoDev and ViaEnder");
                    }
                } else {
                    if (Main.getInstance().experimental == true) {
                        p.sendMessage(
                                Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                        .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                        + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                    } else if (Main.getInstance().latest == true) {
                        p.sendMessage(
                                Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                        .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                        + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                    } else {
                        p.sendMessage(
                                Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                        .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                        + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                        p.sendMessage(
                                Main.getPrefix() + ChatColor.GRAY + "/staffcore gui " + ChatColor.DARK_GRAY + "| "
                                        + ChatColor.GRAY + "To open the adminconsole");
                    }
                    p.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "Created by: " + ChatColor.GRAY
                            + "LacoDev and ViaEnder");
                }
            } else {
                p.sendMessage("");
                if (Main.getInstance().experimental == true) {
                    p.sendMessage(
                            Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                    .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                } else if (Main.getInstance().latest == true) {
                    p.sendMessage(
                            Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                    .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                } else {
                    p.sendMessage(
                            Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                    .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                    p.sendMessage(
                            Main.getPrefix() + ChatColor.GRAY + "/staffcore gui " + ChatColor.DARK_GRAY + "| "
                                    + ChatColor.GRAY + "To open the adminconsole");
                }
                p.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "Created by: " + ChatColor.GRAY
                        + "LacoDev and ViaEnder");
                p.sendMessage("");
            }

        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {

                    SystemManager.reloadStaffCore(sender);

                } else if (args[0].equalsIgnoreCase("update")) {

                    if (!Main.getInstance().latest) {
                        if (!Main.getInstance().experimental) {
                            SystemManager.downloadLatestVersion(sender);
                        } else {
                            sender.sendMessage(
                                    Main.getPrefix() + Main.getMSG("Messages.System.Version.Experimental"));
                        }
                    } else {
                        sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Version.Latest"));
                    }
                }
            } else {
                sender.sendMessage("");
                if (Main.getInstance().experimental == true) {
                    sender.sendMessage(
                            Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                    .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                } else if (Main.getInstance().latest == true) {
                    sender.sendMessage(
                            Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                    .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                } else {
                    sender.sendMessage(
                            Main.getPrefix() + ChatColor.DARK_GRAY + "StaffCore: " + ChatColor.GRAY + "v" + Main
                                    .getInstance().getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                    sender.sendMessage(
                            Main.getPrefix() + ChatColor.GRAY + "/staffcore gui " + ChatColor.DARK_GRAY + "| "
                                    + ChatColor.GRAY + "To open the adminconsole");
                }
                sender.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "Created by: " + ChatColor.GRAY
                        + "LacoDev and ViaEnder");
                sender.sendMessage("");
            }
        }

        return true;
    }

}
