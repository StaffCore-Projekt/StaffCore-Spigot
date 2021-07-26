package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class BanHistoryCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public BanHistoryCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("banhistory").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("BanHistory.See"))) {
                if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                    if (args.length == 1) {
                        if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
                                            "SELECT * FROM ReportSystem_banhistory WHERE BANNED_UUID = '" + staffCore.getStaffCoreLoader().getSystemManager()
                                                    .getUUIDByName(args[0]) + "'");
                                    try {
                                        if (rs.next()) {
                                            p.sendMessage("");
                                            p.sendMessage(
                                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Title")
                                                            + args[0]);
                                            p.sendMessage("");
                                            if (rs.getFetchSize() > 10) {
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.History-too-long"));
                                            }
                                            rs.setFetchSize(10);
                                            while (rs.next()) {
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Prefix.BanID") + rs.getInt("id"));
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Prefix.Reason") + rs
                                                        .getString("REASON"));
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Prefix.BannedBy") + staffCore.getStaffCoreLoader().getSystemManager()
                                                        .getUsernameByUUID(rs.getString("TEAM_UUID")));
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Prefix.BannedSince") + new Date(
                                                        rs.getLong("BAN_START")) + " " + checkForExpiration(
                                                        rs.getString("BANNED_UUID"), rs.getString("REASON"),
                                                        rs.getLong("BAN_END")));
                                                p.sendMessage("");
                                            }
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.No-History-Available"));
                                        }
                                        p.sendMessage("");
                                    } catch (SQLException e) {
                                        Bukkit.getConsoleSender().sendMessage("");
                                        Bukkit.getConsoleSender().sendMessage(
                                                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "� " + ChatColor.RED
                                                        + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "("
                                                        + ChatColor.GRAY + "Fetch BanHistory" + ChatColor.DARK_GRAY + ")");
                                        Bukkit.getConsoleSender().sendMessage("");
                                    }
                                }
                            }.runTaskAsynchronously(staffCore);
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Target-Never-Joined"));
                        }
                    } else if (args.length == 2) {
                        if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                            if (args[1].equalsIgnoreCase("clear")) {
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        staffCore.getStaffCoreLoader().getMySQL().update(
                                                "DELETE FROM ReportSystem_banhistory WHERE BANNED_UUID = '" + staffCore.getStaffCoreLoader().getSystemManager()
                                                        .getUUIDByName(args[0]) + "'");
                                    }

                                }.runTaskAsynchronously(staffCore);
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Cleared"));
                            }
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Target-Never-Joined"));
                        }
                    } else {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Usage"));
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("BanHistory.See")));
            }
        } else {
            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length == 1) {
                    if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
                                        "SELECT * FROM ReportSystem_banhistory WHERE BANNED_UUID = '" + staffCore.getStaffCoreLoader().getSystemManager()
                                                .getUUIDByName(args[0]) + "'");
                                try {
                                    if (rs.next()) {
                                        sender.sendMessage("");
                                        sender.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Title")
                                                        + args[0]);
                                        sender.sendMessage("");
                                        if (rs.getFetchSize() > 10) {
                                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.History-too-long"));
                                        }
                                        rs.setFetchSize(10);
                                        while (rs.next()) {
                                            sender.sendMessage(
                                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Prefix.BanID")
                                                            + rs.getInt("id"));
                                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Prefix.Reason") + rs
                                                    .getString("REASON"));
                                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Prefix.BannedBy") + staffCore.getStaffCoreLoader().getSystemManager()
                                                    .getUsernameByUUID(rs.getString("TEAM_UUID")));
                                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Prefix.BannedSince") + new Date(
                                                    rs.getLong("BAN_START")) + " " + checkForExpiration(
                                                    rs.getString("BANNED_UUID"), rs.getString("REASON"),
                                                    rs.getLong("BAN_END")));
                                            sender.sendMessage("");
                                        }
                                    } else {
                                        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.No-History-Available"));
                                    }
                                    sender.sendMessage("");
                                } catch (SQLException e) {
                                    Bukkit.getConsoleSender().sendMessage("");
                                    Bukkit.getConsoleSender().sendMessage(
                                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.RED
                                                    + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                                    + "Fetch BanHistory" + ChatColor.DARK_GRAY + ")");
                                    Bukkit.getConsoleSender().sendMessage("");
                                }
                            }
                        }.runTaskAsynchronously(staffCore);
                    } else {
                        sender.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Target-Never-Joined"));
                    }
                } else if (args.length == 2) {
                    if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                        if (args[1].equalsIgnoreCase("clear")) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    staffCore.getStaffCoreLoader().getMySQL().update(
                                            "DELETE FROM ReportSystem_banhistory WHERE BANNED_UUID = '" + staffCore.getStaffCoreLoader().getSystemManager()
                                                    .getUUIDByName(args[0]) + "'");
                                }

                            }.runTaskAsynchronously(staffCore);
                            sender.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Cleared"));
                        }
                    } else {
                        sender.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Target-Never-Joined"));
                    }
                } else {
                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Usage"));
                }
            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        }
        return false;
    }

    protected String checkForExpiration(String uuid, String reason, long long1) {
        if (System.currentTimeMillis() > long1) {
            return ChatColor.DARK_GRAY + "(" + ChatColor.RED + "Expired" + ChatColor.DARK_GRAY + ")";
        } else {
            if (staffCore.getStaffCoreLoader().getBanManager().isBanned(uuid)) {
                if (staffCore.getStaffCoreLoader().getBanManager().getBanReason(uuid).matches(reason)) {
                    if (staffCore.getStaffCoreLoader().getBanManager().getBanEnd(uuid) == -1) {
                        return ChatColor.DARK_GRAY + "(" + staffCore.getStaffCoreLoader().getMessage("Layout.Ban.Length-Values.Permanently")
                                + ChatColor.DARK_GRAY + ")";
                    } else {
                        return ChatColor.DARK_GRAY + "(" + staffCore.getStaffCoreLoader().getBanManager().getBanFinalEnd(uuid) + ChatColor.DARK_GRAY
                                + ")";
                    }
                } else {
                    return staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Status.Unbanned");
                }
            } else {
                return staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.History.Status.Unbanned");
            }
        }
    }
}
