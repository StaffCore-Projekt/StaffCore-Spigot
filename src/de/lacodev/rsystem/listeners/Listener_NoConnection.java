package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listener_NoConnection implements Listener {

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();

    if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
      p.sendTitle(ChatColor.RED + "MySQL Connection invalid",
          ChatColor.GRAY + "Please make sure to enter your data in mysql.yml");
    }
  }

}
