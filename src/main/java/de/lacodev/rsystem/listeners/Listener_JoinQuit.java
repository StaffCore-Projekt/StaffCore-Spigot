package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.*;
import me.vagdedes.spartan.system.Enums.HackType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Listener_JoinQuit implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        SystemManager.createPlayerData(p);

        if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p
                .hasPermission(Main.getPermissionNotice("Permissions.System.Notify"))) {
            if (!Main.getInstance().latest) {
                if (!Main.getInstance().experimental) {
                    p.sendMessage("");
                    p.sendMessage(Main.getPrefix() + ChatColor.RED + "There is an update available!");

                    TextComponent tc = new TextComponent();
                    tc.setText(Main.getPrefix() + ChatColor.GRAY + "Download: ");

                    TextComponent tc2 = new TextComponent();
                    tc2.setText(ChatColor.RED + "Click here");
                    tc2.setClickEvent(new ClickEvent(Action.OPEN_URL,
                            "https://www.spigotmc.org/resources/staffcore-1-7-1-15.48655/updates"));
                    tc.addExtra(tc2);
                    p.spigot().sendMessage(tc);
                    p.sendMessage("");
                    if (Bukkit.getVersion().contains("(MC: 1.15.2)") || Bukkit.getVersion()
                            .contains("(MC: 1.15.1)") || Bukkit.getVersion().contains("(MC: 1.15)") || Bukkit
                            .getVersion().contains("(MC: 1.14.4)")) {
                        p.playSound(p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 100, 100);
                    } else if (Bukkit.getVersion().contains("(MC: 1.8.8)") || Bukkit.getVersion()
                            .contains("(MC: 1.8.9)") || Bukkit.getVersion().contains("(MC: 1.9)")) {
                        p.playSound(p.getLocation(), Sound.valueOf("ANVIL_LAND"), 100, 100);
                    }
                } else {
                    p.sendMessage("");
                    p.sendMessage(
                            Main.getPrefix() + ChatColor.GRAY + "You are using an " + ChatColor.LIGHT_PURPLE
                                    + "experimental build" + ChatColor.GRAY + "!");
                    p.sendMessage(
                            Main.getPrefix() + ChatColor.GRAY + "If you find any errors, please report them!");
                    p.sendMessage("");
                }
            }
        }

        if (MySQL.isConnected()) {
            int i = 0;

            ResultSet rs = MySQL.getResult(
                    "SELECT * FROM ReportSystem_actionsdb WHERE EXECUTOR_UUID = '" + p.getUniqueId()
                            .toString() + "'");
            try {
                if (rs.next()) {
                    i = rs.getFetchSize() + 1;
                    if (rs.getString("ACTION").matches("REPORT_SUCCESS")) {
                        ActionManager.deleteAction(rs.getInt("id"));
                        if (Bukkit.getVersion().contains("(MC: 1.15.2)") || Bukkit.getVersion()
                                .contains("(MC: 1.15.1)") || Bukkit.getVersion().contains("(MC: 1.15)") || Bukkit
                                .getVersion().contains("(MC: 1.14.4)")) {
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 100);
                        } else if (Bukkit.getVersion().contains("(MC: 1.8.8)") || Bukkit.getVersion()
                                .contains("(MC: 1.8.9)") || Bukkit.getVersion().contains("(MC: 1.9)")) {
                            p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 100, 100);
                        }
                        p.sendMessage(Main.getPrefix() + Main
                                .getMSG("Messages.Vault.Rewards.Report.Success-While-Offline")
                                .replace("%count%", String.valueOf(i)));
                    } else if (rs.getString("ACTION").matches("CLAIM_REPORT")) {
                        ReportManager.claimReport(p, rs.getString("DESCRIPTION"));
                        ActionManager.deleteAction(rs.getInt("id"));
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (Main.getInstance().getConfig()
                .getBoolean("MatrixAntiCheat.Autoreport.Log.Reset-Violations-On-Join")) {
            File file = new File(
                    "plugins//" + Main.getInstance().getDescription().getName() + "//logs//matrixlog.yml");
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            if (Main.getInstance().setupMatrix()) {
                if (cfg.contains("Log." + p.getUniqueId().toString())) {
                    for (me.rerere.matrix.api.HackType type : me.rerere.matrix.api.HackType.values()) {
                        if (cfg.contains(
                                "Log." + p.getUniqueId().toString() + "." + type.toString().toUpperCase())) {
                            cfg.set(
                                    "Log." + p.getUniqueId().toString() + "." + type.toString().toUpperCase() + ".VL",
                                    0);
                            try {
                                cfg.save(file);
                            } catch (IOException e1) {
                                Bukkit.getConsoleSender().sendMessage("");
                                Bukkit.getConsoleSender().sendMessage(
                                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "� " + ChatColor.RED
                                                + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                                + "Save MatrixLog" + ChatColor.DARK_GRAY + ")");
                                Bukkit.getConsoleSender().sendMessage("");
                            }
                        }
                    }
                }
            }
        }

        if (Main.getInstance().getConfig()
                .getBoolean("SpartanAntiCheat.Autoreport.Log.Reset-Violations-On-Join")) {
            File file = new File(
                    "plugins//" + Main.getInstance().getDescription().getName() + "//logs//spartanlog.yml");
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            if (Main.getInstance().setupSpartanAC()) {
                if (cfg.contains("Log." + p.getUniqueId().toString())) {
                    for (HackType type : HackType.values()) {
                        if (cfg.contains(
                                "Log." + p.getUniqueId().toString() + "." + type.toString().toUpperCase())) {
                            cfg.set(
                                    "Log." + p.getUniqueId().toString() + "." + type.toString().toUpperCase() + ".VL",
                                    0);
                            try {
                                cfg.save(file);
                            } catch (IOException e1) {
                                Bukkit.getConsoleSender().sendMessage("");
                                Bukkit.getConsoleSender().sendMessage(
                                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "� " + ChatColor.RED
                                                + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                                + "Save SpartanLog" + ChatColor.DARK_GRAY + ")");
                                Bukkit.getConsoleSender().sendMessage("");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler

    public void onQuit(PlayerQuitEvent e) {

        PageManager.page.remove(e.getPlayer());

        BanManager.unfreeze(e.getPlayer());

    }
}