package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.mysql.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Listener_ChatLog implements Listener {

  @EventHandler(priority = EventPriority.LOWEST)
  public void onChatLog(AsyncPlayerChatEvent e) {
    Player p = e.getPlayer();
    if (!e.isCancelled()) {
      MySQL.update(
          "INSERT INTO ReportSystem_messages(SENDER_UUID,MESSAGE) VALUES ('" + p.getUniqueId()
              .toString() + "','" + e.getMessage() + "')");
    }
  }
}
