package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ReportCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public ReportCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("report").setExecutor(this);
    }

    public void sendHelp(Player p) {
        p.sendMessage("");
        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Usage.Title"));
        p.sendMessage("");
        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Usage.Command"));
        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Usage.Inventory"));
        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Usage.Template"));
        p.sendMessage("");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length == 0) {
                    sendHelp(p);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("templates")) {
                        p.sendMessage("");
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.YELLOW + "Report Templates");
                        p.sendMessage("");
                        ArrayList<String> reasons = staffCore.getStaffCoreLoader().getReportManager().getReportReasons();

                        if (reasons.size() > 0) {
                            for (int i = 0; i < reasons.size(); i++) {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons
                                                .get(i));
                            }
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.No-Reportreasons"));
                        }
                        p.sendMessage("");
                    } else {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null) {
                            if (target != p) {
                                staffCore.getStaffCoreLoader().getReportManager().openPagedReportInv(p, target.getName(), 1);
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Cannot-report-self"));
                            }
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Target-offline"));
                        }
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("claim")) {

                        if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Report.Claim")) || p
                                .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                            staffCore.getStaffCoreLoader().getReportManager().claimReport(p, args[1]);
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                    .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Report.Claim")));
                        }

                    } else {
                        ArrayList<String> reasons = staffCore.getStaffCoreLoader().getReportManager().getReportReasons();

                        if (reasons.contains(args[1].toLowerCase())) {
                            Player target = Bukkit.getPlayer(args[0]);

                            if (target != null) {
                                if (target != p) {
                                    staffCore.getStaffCoreLoader().getReportManager().createReport(p.getName(), target, args[1]);
                                    if (!p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || !p
                                            .hasPermission(staffCore.getStaffCoreLoader().getPermission("Report.Spam.Bypass"))) {
                                        ChatListener.spam.put(p, System.currentTimeMillis()
                                                + staffCore.getStaffCoreLoader().getConfigProvider().getInt("Report-Spam.Duration-in-Seconds")
                                                * 1000);
                                    }
                                } else {
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Cannot-report-self"));
                                }
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Target-offline"));
                            }
                        } else {
                            sendHelp(p);
                        }
                    }
                } else {
                    sendHelp(p);
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
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
