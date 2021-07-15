package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.SystemManager;
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

public class CMD_BanList implements CommandExecutor {

    public ArrayList<String> banned_players = new ArrayList<>();
    public HashMap<String, String> ban_reasons = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p
                    .hasPermission(Main.getPermissionNotice("Permissions.Ban.List"))) {

                if (MySQL.isConnected()) {
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_bansdb");
                            try {
                                if (rs.next()) {
                                    if (rs.getFetchSize() > 1) {
                                        while (rs.next()) {
                                            if (!banned_players
                                                    .contains(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")))) {
                                                banned_players
                                                        .add(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")));
                                                ban_reasons
                                                        .put(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")),
                                                                rs.getString("REASON"));
                                            }
                                        }
                                    } else {
                                        if (!banned_players
                                                .contains(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")))) {
                                            banned_players
                                                    .add(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")));
                                            ban_reasons.put(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")),
                                                    rs.getString("REASON"));
                                        }
                                    }
                                    p.sendMessage("");
                                    for (int i = 0; i < banned_players.size(); i++) {
                                        p.sendMessage(
                                                Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ""
                                                        + banned_players.get(i) + " " + ChatColor.DARK_GRAY + "� "
                                                        + ChatColor.GRAY + "" + ban_reasons.get(banned_players.get(i)));
                                    }
                                    banned_players.clear();
                                    ban_reasons.clear();
                                    p.sendMessage("");
                                } else {
                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.List.Empty"));
                                }
                            } catch (SQLException e) {
                                Bukkit.getConsoleSender().sendMessage("");
                                Bukkit.getConsoleSender().sendMessage(
                                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "� " + ChatColor.RED + ""
                                                + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                                + "Fetch BanList" + ChatColor.DARK_GRAY + ")");
                                Bukkit.getConsoleSender().sendMessage("");
                            }
                        }
                    }.runTaskAsynchronously(Main.getInstance());
                } else {
                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
                }
            } else {
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
                        .replace("%permission%", Main.getPermissionNotice("Permissions.Ban.List")));
            }

        } else {
            if (MySQL.isConnected()) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_bansdb");
                        try {
                            if (rs.next()) {
                                if (rs.getFetchSize() > 1) {
                                    while (rs.next()) {
                                        if (!banned_players
                                                .contains(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")))) {
                                            banned_players
                                                    .add(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")));
                                            ban_reasons.put(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")),
                                                    rs.getString("REASON"));
                                        }
                                    }
                                } else {
                                    if (!banned_players
                                            .contains(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")))) {
                                        banned_players
                                                .add(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")));
                                        ban_reasons.put(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")),
                                                rs.getString("REASON"));
                                    }
                                }
                                sender.sendMessage("");
                                for (int i = 0; i < banned_players.size(); i++) {
                                    sender.sendMessage(
                                            Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ""
                                                    + banned_players.get(i) + " " + ChatColor.DARK_GRAY + "� "
                                                    + ChatColor.GRAY + "" + ban_reasons.get(banned_players.get(i)));
                                }
                                sender.sendMessage("");
                            } else {
                                sender
                                        .sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.List.Empty"));
                            }
                        } catch (SQLException e) {
                            Bukkit.getConsoleSender().sendMessage("");
                            Bukkit.getConsoleSender().sendMessage(
                                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "� " + ChatColor.RED + ""
                                            + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                            + "Fetch BanList" + ChatColor.DARK_GRAY + ")");
                            Bukkit.getConsoleSender().sendMessage("");
                        }
                    }
                }.runTaskAsynchronously(Main.getInstance());
            } else {
                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
            }
        }
        return true;
    }

}
