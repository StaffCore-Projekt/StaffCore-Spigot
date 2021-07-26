package de.lacodev.rsystem.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportManagerCommand implements CommandExecutor {
    private final StaffCore staffCore;

    public ReportManagerCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("reportmanager").setExecutor(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("addreason")) {
                        if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("ReportManager.addreason"))
                                || p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                            if (Bukkit.getVersion().contains("(MC: 1.8.8)")) {
                                if (p.getItemInHand().getType() != Material.AIR) {
                                    if (!staffCore.getStaffCoreLoader().getReportManager().existsReportReason(args[1])) {
                                        staffCore.getStaffCoreLoader().getReportManager().createReportReason(args[1], p.getItemInHand(), p.getName());
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.Success"));
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.Success"));
                                        }
                                    } else {
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.Already-Exists"));
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.Already-Exists"));
                                        }
                                    }
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
                                    }
                                }
                            } else {
                                if (p.getItemInHand().getType() != Material.AIR) {
                                    if (!staffCore.getStaffCoreLoader().getReportManager().existsReportReason(args[1])) {
                                        staffCore.getStaffCoreLoader().getReportManager().createReportReason(args[1], p.getItemInHand(),
                                                p.getName());
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.Success"));
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.Success"));
                                        }
                                    } else {
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.Already-Exists"));
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.Already-Exists"));
                                        }
                                    }
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
                                    }
                                }
                            }
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                    .replace("%permission%",
                                            staffCore.getStaffCoreLoader().getPermission("ReportManager.addreason")));
                        }
                    } else if (args[0].equalsIgnoreCase("removereason")) {
                        if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("ReportManager.removereason"))
                                || p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                            if (staffCore.getStaffCoreLoader().getReportManager().existsReportReason(args[1])) {
                                staffCore.getStaffCoreLoader().getReportManager().deleteReportReason(args[1], p.getName());
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Remove-Reason.Success"));
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Remove-Reason.Success"));
                                }
                            } else {
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Remove-Reason.Not-Exists"));
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.ReportManager.Remove-Reason.Not-Exists"));
                                }
                            }
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                    .replace("%permission%",
                                            staffCore.getStaffCoreLoader().getPermission("ReportManager.removereason")));
                        }
                    } else {
                        p.sendMessage("");
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "ReportManager"
                                        + ChatColor.DARK_GRAY + "]");
                        p.sendMessage("");
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/reportmanager addreason <Reason>");
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/reportmanager removereason <Reason>");
                        p.sendMessage("");
                    }
                } else {
                    p.sendMessage("");
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "ReportManager"
                                    + ChatColor.DARK_GRAY + "]");
                    p.sendMessage("");
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/reportmanager addreason <Reason>");
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/reportmanager removereason <Reason>");
                    p.sendMessage("");
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
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
