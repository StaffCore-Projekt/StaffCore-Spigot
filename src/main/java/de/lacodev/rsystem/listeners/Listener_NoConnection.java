package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listener_NoConnection implements Listener {

    private final StaffCore staffCore;

    public Listener_NoConnection(StaffCore staffCore) {
        this.staffCore = staffCore;
        Bukkit.getPluginManager().registerEvents(this, staffCore);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
            p.sendTitle(ChatColor.RED + "MySQL Connection invalid",
                    ChatColor.GRAY + "Please make sure to enter your data in mysql.yml");
        }
    }

}
