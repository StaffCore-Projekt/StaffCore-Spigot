package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.utils.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;

public class Listener_Login implements Listener {
	private final SettingsManager settings = new SettingsManager();
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (settings.isKey("maintenance")){
			if (settings.getBoolean("maintenance")){
				if (!(p.hasPermission(Main.getPermissionNotice("Permissions.Maintenance.Join")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything")))){
					// TODO: 18.01.2021 Insert Translator 
					e.disallow(Result.KICK_FULL, "Sorry but we are currently in Maintenance!");
					return;
				}
			}
		}
		
		if(BanManager.isBanned(p.getUniqueId().toString())) {
			
			if(BanManager.getBanEnd(p.getUniqueId().toString()) != -1) {
				e.disallow(Result.KICK_BANNED, Main.getMSG("Messages.Layouts.Ban").replace("%reason%", BanManager.getBanReason(p.getUniqueId().toString())).replace("%remaining%", BanManager.getBanFinalEnd(p.getUniqueId().toString())).replace("%lengthvalue%", Main.getMSG("Messages.Layouts.Ban.Length-Values.Temporarly")));
			} else {
				e.disallow(Result.KICK_BANNED, Main.getMSG("Messages.Layouts.Ban").replace("%reason%", BanManager.getBanReason(p.getUniqueId().toString())).replace("%remaining%", Main.getMSG("Messages.Layouts.Ban.Length-Values.Permanently")).replace("%lengthvalue%", Main.getMSG("Messages.Layouts.Ban.Length-Values.Permanently")));
			}
			
		} else {
			if(!BanManager.isIPBanned(e.getRealAddress().toString())) {
				if(p.isOp()) {
					if(!p.hasPermission(Main.getPermissionNotice("Permissions.Allow-OP.Join"))) {
						e.disallow(Result.KICK_OTHER, Main.getMSG("Messages.System.Unpermitted-OP.Kick-Player"));
						for(Player all : Bukkit.getOnlinePlayers()) {
							if(all.hasPermission(Main.getPermissionNotice("Permissions.Allow-OP.Notify"))) {
								all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Unpermitted-OP.Notify").replace("%target%", p.getName()));
							}
						}
					}
				} else {
					if(Main.getAntiMCLeaksHandler().isAccountCached(p.getUniqueId().toString())) {
						e.disallow(Result.KICK_OTHER, Main.getMSG("Messages.System.MCLeaks-Blocker.Blocked-Accounts.Kick"));
					}
				}
			} else {
				e.disallow(Result.KICK_BANNED, Main.getMSG("Messages.Ban-System.IP-Ban.Kick-Screen"));
			}
		}
	}
	
}
