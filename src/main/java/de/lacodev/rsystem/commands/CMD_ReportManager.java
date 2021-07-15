package de.lacodev.rsystem.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_ReportManager implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (MySQL.isConnected()) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("addreason")) {
                        if (p.hasPermission(Main.getPermissionNotice("Permissions.ReportManager.addreason"))
                                || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
                            if (Bukkit.getVersion().contains("(MC: 1.8.8)")) {
                                if (p.getItemInHand().getType() != Material.AIR) {
                                    if (!ReportManager.existsReportReason(args[1])) {
                                        ReportManager.createReportReason(args[1], p.getItemInHand(), p.getName());
                                        if (Main.getInstance().actionbar) {
                                            ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main
                                                    .getMSG("Messages.ReportManager.Add-Reason.Success"));
                                        } else {
                                            p.sendMessage(Main.getPrefix() + Main
                                                    .getMSG("Messages.ReportManager.Add-Reason.Success"));
                                        }
                                    } else {
                                        if (Main.getInstance().actionbar) {
                                            ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main
                                                    .getMSG("Messages.ReportManager.Add-Reason.Already-Exists"));
                                        } else {
                                            p.sendMessage(Main.getPrefix() + Main
                                                    .getMSG("Messages.ReportManager.Add-Reason.Already-Exists"));
                                        }
                                    }
                                } else {
                                    if (Main.getInstance().actionbar) {
                                        ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main
                                                .getMSG("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
                                    } else {
                                        p.sendMessage(Main.getPrefix() + Main
                                                .getMSG("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
                                    }
                                }
                            } else {
                                if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                    if (!ReportManager.existsReportReason(args[1])) {
                                        ReportManager.createReportReason(args[1], p.getInventory().getItemInMainHand(),
                                                p.getName());
                                        if (Main.getInstance().actionbar) {
                                            ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main
                                                    .getMSG("Messages.ReportManager.Add-Reason.Success"));
                                        } else {
                                            p.sendMessage(Main.getPrefix() + Main
                                                    .getMSG("Messages.ReportManager.Add-Reason.Success"));
                                        }
                                    } else {
                                        if (Main.getInstance().actionbar) {
                                            ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main
                                                    .getMSG("Messages.ReportManager.Add-Reason.Already-Exists"));
                                        } else {
                                            p.sendMessage(Main.getPrefix() + Main
                                                    .getMSG("Messages.ReportManager.Add-Reason.Already-Exists"));
                                        }
                                    }
                                } else {
                                    if (Main.getInstance().actionbar) {
                                        ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main
                                                .getMSG("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
                                    } else {
                                        p.sendMessage(Main.getPrefix() + Main
                                                .getMSG("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
                                    }
                                }
                            }
                        } else {
                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
                                    .replace("%permission%",
                                            Main.getPermissionNotice("Permissions.ReportManager.addreason")));
                        }
                    } else if (args[0].equalsIgnoreCase("removereason")) {
                        if (p.hasPermission(Main.getPermissionNotice("Permissions.ReportManager.removereason"))
                                || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
                            if (ReportManager.existsReportReason(args[1])) {
                                ReportManager.deleteReportReason(args[1], p.getName());
                                if (Main.getInstance().actionbar) {
                                    ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main
                                            .getMSG("Messages.ReportManager.Remove-Reason.Success"));
                                } else {
                                    p.sendMessage(Main.getPrefix() + Main
                                            .getMSG("Messages.ReportManager.Remove-Reason.Success"));
                                }
                            } else {
                                if (Main.getInstance().actionbar) {
                                    ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main
                                            .getMSG("Messages.ReportManager.Remove-Reason.Not-Exists"));
                                } else {
                                    p.sendMessage(Main.getPrefix() + Main
                                            .getMSG("Messages.ReportManager.Remove-Reason.Not-Exists"));
                                }
                            }
                        } else {
                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
                                    .replace("%permission%",
                                            Main.getPermissionNotice("Permissions.ReportManager.removereason")));
                        }
                    } else {
                        p.sendMessage("");
                        p.sendMessage(
                                Main.getPrefix() + ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "ReportManager"
                                        + ChatColor.DARK_GRAY + "]");
                        p.sendMessage("");
                        p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "/reportmanager addreason <Reason>");
                        p.sendMessage(
                                Main.getPrefix() + ChatColor.GRAY + "/reportmanager removereason <Reason>");
                        p.sendMessage("");
                    }
                } else {
                    p.sendMessage("");
                    p.sendMessage(
                            Main.getPrefix() + ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "ReportManager"
                                    + ChatColor.DARK_GRAY + "]");
                    p.sendMessage("");
                    p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "/reportmanager addreason <Reason>");
                    p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "/reportmanager removereason <Reason>");
                    p.sendMessage("");
                }
            } else {
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "� " + ChatColor.RED + ChatColor.BOLD
                            + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Console-Input"
                            + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }
        return true;
    }

}
