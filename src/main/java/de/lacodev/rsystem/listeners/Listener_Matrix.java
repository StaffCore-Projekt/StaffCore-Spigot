package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.ReportManager;
import me.rerere.matrix.api.events.PlayerViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;

public class Listener_Matrix implements Listener {

    @EventHandler
    public void onVL(PlayerViolationEvent e) {
        Player p = e.getPlayer();

        File file = new File(
                "plugins//" + Main.getInstance().getDescription().getName() + "//logs//matrix-log.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (Main.getInstance().getConfig().getBoolean("MatrixAntiCheat.Autoreport.Enable")) {
            if (cfg.contains("Log." + p.getUniqueId().toString())) {
                if (cfg.contains("Log." + p.getUniqueId().toString() + "." + e.getHackType().toString())) {
                    int currentvl = cfg.getInt(
                            "Log." + p.getUniqueId().toString() + "." + e.getHackType().toString() + ".VL");
                    int newvl = currentvl + e.getViolations();

                    cfg.set("Log." + p.getUniqueId().toString() + "." + e.getHackType().toString() + ".VL",
                            newvl);
                    try {
                        cfg.save(file);
                    } catch (IOException e1) {
                        Bukkit.getConsoleSender().sendMessage("");
                        Bukkit.getConsoleSender().sendMessage(
                                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "�  " + ChatColor.RED
                                        + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                        + "Save MatrixLog" + ChatColor.DARK_GRAY + ")");
                        Bukkit.getConsoleSender().sendMessage("");
                    }

                    if (newvl >= Main.getInstance().getConfig().getInt(
                            "MatrixAntiCheat.Autoreport." + e.getHackType().toString() + ".Violationslevel")) {
                        ReportManager
                                .createReport(Main.getPermissionNotice("MatrixAntiCheat.Autoreport.Name"), p,
                                        Main.getPermissionNotice(
                                                "MatrixAntiCheat.Autoreport." + e.getHackType().toString()
                                                        + ".Displayname"));
                    }
                } else {
                    cfg.set("Log." + p.getUniqueId().toString() + "." + e.getHackType().toString() + ".VL",
                            e.getViolations());
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
            } else {
                cfg.set("Log." + p.getUniqueId().toString() + "." + e.getHackType().toString() + ".VL",
                        e.getViolations());
                try {
                    cfg.save(file);
                } catch (IOException e1) {
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "� " + ChatColor.RED
                                    + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                    + "Save MatrixLog" + ChatColor.GRAY + ")");
                    Bukkit.getConsoleSender().sendMessage("");
                }
            }
        }
    }

}
