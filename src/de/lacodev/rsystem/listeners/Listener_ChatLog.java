package de.lacodev.rsystem.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.lacodev.rsystem.mysql.MySQL;

public class Listener_ChatLog implements Listener {
	
	@EventHandler
	public void onChatLog(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		MySQL.update("INSERT INTO ReportSystem_messages(SENDER_UUID,MESSAGE) VALUES ('"+ p.getUniqueId().toString() +"','"+ e.getMessage() +"')");
	}
}
