package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;

import java.net.MalformedURLException;
import java.net.URL;

@RequiredArgsConstructor
public class ConsoleManager {
    private final StaffCore staffCore;

    private ConsoleCommandSender consoleSender;

    public void init(){
        this.consoleSender = Bukkit.getConsoleSender();
    }

    public void startOutput(){
        checkVersion();
        this.staffCore.getStaffCoreLoader().initHooks();
        this.staffCore.getStaffCoreLoader().checkMatrix();
        this.staffCore.getStaffCoreLoader().getMySQL().connect();
        this.staffCore.getStaffCoreLoader().getMySQL().createTable();
        for (Material value : Material.values()) {
            this.staffCore.getStaffCoreLoader().getMySQL()  .updateMaterial(value);
        }
    }

    public void checkVersion(){
        try {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB Checking version... (" + ChatColor.GRAY + staffCore.getDescription().getVersion() + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
            staffCore.getStaffCoreLoader().getSystemManager().checkLizenz(new URL("https://api.lacodev.de/staffcore/versions/"));
        }catch (MalformedURLException e) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.RED + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Versioncheck" + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }
    }
}
