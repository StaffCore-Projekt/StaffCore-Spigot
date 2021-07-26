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
import java.util.ArrayList;
import java.util.HashMap;

public class BanListCommand implements CommandExecutor {

    private final StaffCore staffCore;
    public ArrayList<String> banned_players = new ArrayList<>();
    public HashMap<String, String> ban_reasons = new HashMap<>();

    public BanListCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("banlist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("Ban.List"))) {

                if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT * FROM ReportSystem_bansdb");
                            try {
                                if (rs.next()) {
                                    if (rs.getFetchSize() > 1) {
                                        while (rs.next()) {
                                            if (!banned_players
                                                    .contains(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")))) {
                                                banned_players
                                                        .add(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")));
                                                ban_reasons
                                                        .put(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")),
                                                                rs.getString("REASON"));
                                            }
                                        }
                                    } else {
                                        if (!banned_players
                                                .contains(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")))) {
                                            banned_players
                                                    .add(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")));
                                            ban_reasons.put(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")),
                                                    rs.getString("REASON"));
                                        }
                                    }
                                    p.sendMessage("");
                                    for (int i = 0; i < banned_players.size(); i++) {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ""
                                                        + banned_players.get(i) + " " + ChatColor.DARK_GRAY + "\u00BB "
                                                        + ChatColor.GRAY + "" + ban_reasons.get(banned_players.get(i)));
                                    }
                                    banned_players.clear();
                                    ban_reasons.clear();
                                    p.sendMessage("");
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.List.Empty"));
                                }
                            } catch (SQLException e) {
                                Bukkit.getConsoleSender().sendMessage("");
                                Bukkit.getConsoleSender().sendMessage(
                                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.RED + ""
                                                + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                                + "Fetch BanList" + ChatColor.DARK_GRAY + ")");
                                Bukkit.getConsoleSender().sendMessage("");
                            }
                        }
                    }.runTaskAsynchronously(staffCore);
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Ban.List")));
            }

        } else {
            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT * FROM ReportSystem_bansdb");
                        try {
                            if (rs.next()) {
                                if (rs.getFetchSize() > 1) {
                                    while (rs.next()) {
                                        if (!banned_players
                                                .contains(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")))) {
                                            banned_players
                                                    .add(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")));
                                            ban_reasons.put(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")),
                                                    rs.getString("REASON"));
                                        }
                                    }
                                } else {
                                    if (!banned_players
                                            .contains(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")))) {
                                        banned_players
                                                .add(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")));
                                        ban_reasons.put(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("BANNED_UUID")),
                                                rs.getString("REASON"));
                                    }
                                }
                                sender.sendMessage("");
                                for (int i = 0; i < banned_players.size(); i++) {
                                    sender.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ""
                                                    + banned_players.get(i) + " " + ChatColor.DARK_GRAY + "\u00BB "
                                                    + ChatColor.GRAY + "" + ban_reasons.get(banned_players.get(i)));
                                }
                                sender.sendMessage("");
                            } else {
                                sender
                                        .sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.List.Empty"));
                            }
                        } catch (SQLException e) {
                            Bukkit.getConsoleSender().sendMessage("");
                            Bukkit.getConsoleSender().sendMessage(
                                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.RED + ""
                                            + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                            + "Fetch BanList" + ChatColor.DARK_GRAY + ")");
                            Bukkit.getConsoleSender().sendMessage("");
                        }
                    }
                }.runTaskAsynchronously(staffCore);
            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        }
        return true;
    }

}
