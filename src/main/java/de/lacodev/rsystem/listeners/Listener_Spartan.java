package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.StaffCore;
import me.vagdedes.spartan.api.PlayerViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;

public class Listener_Spartan implements Listener {
    private final StaffCore staffCore;

    public Listener_Spartan(StaffCore staffCore) {
        this.staffCore = staffCore;
        Bukkit.getPluginManager().registerEvents(this, staffCore);
    }

    @EventHandler
    public void onVLSpartan(PlayerViolationEvent e) {
        Player p = e.getPlayer();

        File file = new File(staffCore.getDataFolder() + File.separator + "logs" + File.separator + "spartan-log.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("SpartanAntiCheat.Autoreport.Enable")) {
            if (cfg.contains("Log." + p.getUniqueId().toString())) {
                if (cfg.contains("Log." + p.getUniqueId().toString() + "." + e.getHackType().toString())) {
                    int currentvl = cfg.getInt(
                            "Log." + p.getUniqueId().toString() + "." + e.getHackType().toString() + ".VL");
                    int newvl = currentvl + e.getViolation();

                    cfg.set("Log." + p.getUniqueId().toString() + "." + e.getHackType().toString() + ".VL",
                            newvl);
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

                    if (newvl >= staffCore.getStaffCoreLoader().getConfigProvider().getInt(
                            "SpartanAntiCheat.Autoreport." + e.getHackType().toString() + ".Violationslevel")) {
                        staffCore.getStaffCoreLoader().getReportManager()
                                .createReport(staffCore.getStaffCoreLoader().getMessage("SpartanAntiCheat.Autoreport.Name"), p,
                                        staffCore.getStaffCoreLoader().getMessage(
                                                "SpartanAntiCheat.Autoreport." + e.getHackType().toString()
                                                        + ".Displayname"));
                    }
                } else {
                    cfg.set("Log." + p.getUniqueId().toString() + "." + e.getHackType().toString() + ".VL",
                            e.getViolation());
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
            } else {
                cfg.set("Log." + p.getUniqueId().toString() + "." + e.getHackType().toString() + ".VL",
                        e.getViolation());
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
