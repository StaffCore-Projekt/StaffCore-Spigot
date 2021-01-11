package de.lacodev.rsystem.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.InventoryHandler;
import de.lacodev.rsystem.utils.ReportManager;

public class Listener_Chat implements Listener {
	
	public static HashMap<Player, Long> spam = new HashMap<>();
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		/*String[] args = e.getMessage().split(" ");

		if (args[0].equalsIgnoreCase("@staff")) {

			if(p.hasPermission(Main.getPermissionNotice("Permissions.StaffChat.Chat")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {

				//AB hier ist es offen

				for (Player staffs : Bukkit.getServer().getOnlinePlayers()){
					if(staffs.hasPermission(Main.getPermissionNotice("Permissions.StaffChat.Chat")) || staffs.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
						staffs.sendMessage("§8[§4StaffChat§8]§b " + p.getName() + " §7: " + e.getMessage().replace(args[0], ""));
					}
				}


			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
						.replace("%permission%", Main.getPermissionNotice("Permissions.StaffChat.Chat")));

			}

			e.setCancelled(true);
		}else {*/
		if (BanManager.isGMute()){
			if(!(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Mute.Global.Write")) )) {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Global-Mute-System.Message-when-a-player-writes-on-GlobalMute"));
				e.setCancelled(true);
			}
		}else {
			if (BanManager.isMuted(p.getUniqueId().toString())) {

				p.sendMessage(Main.getMSG("Messages.Layouts.Mute").replace("%reason%", BanManager.getMuteReason(p.getUniqueId().toString())).replace("%remaining%", BanManager.getMuteFinalEnd(p.getUniqueId().toString())));
				e.setCancelled(true);

			}

			File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//chatfilter.yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

			if (cfg.contains("Cursed-Words")) {
				if (wordIsBlocked(e.getMessage())) {
					if (Main.getInstance().getConfig().getBoolean("Chatfilter.Cursed-Words.AutoReport.Enable")) {
						if (Main.getInstance().getConfig().getBoolean("Chatfilter.Cursed-Words.Block-Message.Enable")) {
							ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p, Main.getPermissionNotice("Chatfilter.Cursed-Words.AutoReport.Reason"));
							e.setCancelled(true);
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Cursed-Words.Block"));
						} else {
							ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p, Main.getPermissionNotice("Chatfilter.Cursed-Words.AutoReport.Reason"));
						}
					} else {
						if (Main.getInstance().getConfig().getBoolean("Chatfilter.Cursed-Words.Block-Message")) {
							e.setCancelled(true);
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Cursed-Words.Block"));
						}
					}
				}
			}

			if (InventoryHandler.filter.contains(p)) {

				ArrayList<String> cursed = (ArrayList<String>) cfg.getStringList("Cursed-Words");

				if (!cursed.contains(e.getMessage())) {
					e.setCancelled(true);
					cursed.add(e.getMessage());
					cfg.set("Cursed-Words", cursed);
					try {
						cfg.save(file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					p.sendMessage(Main.getPrefix() + "§7Word §aadded §7to the Cursed-Words");
				} else {
					e.setCancelled(true);
				}

			}

			ArrayList<String> ad = (ArrayList<String>) Main.getInstance().getConfig().getStringList("Chatfilter.Advertisment.Whitelist");
			ArrayList<String> blocked = (ArrayList<String>) Main.getInstance().getConfig().getStringList("Chatfilter.Advertisment.Blocked-Domains");

			String[] adtest = e.getMessage().split(" ");

			for (String ads : adtest) {
				if (!ad.contains(ads)) {
					if (blocked.contains(ads)) {
						if (Main.getInstance().getConfig().getBoolean("Chatfilter.Advertisment.AutoReport.Enable")) {
							if (Main.getInstance().getConfig().getBoolean("Chatfilter.Advertisment.Block-Message.Enable")) {
								ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p, Main.getPermissionNotice("Chatfilter.Advertisment.AutoReport.Reason"));
								e.setCancelled(true);
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Advertisment.Block"));
							} else {
								ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p, Main.getPermissionNotice("Chatfilter.Advertisment.AutoReport.Reason"));
							}
						} else {
							if (Main.getInstance().getConfig().getBoolean("Chatfilter.Advertisment.Block-Message")) {
								e.setCancelled(true);
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Advertisment.Block"));
							}
						}
					}
				}
			}
		}
		//}
	}

	private boolean wordIsBlocked(String message) {
		File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//chatfilter.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		ArrayList<String> cursed = (ArrayList<String>) cfg.getStringList("Cursed-Words");
		
		for(int i = 0; i < cursed.size(); i++) {
			int minpercent = Main.getInstance().getConfig().getInt("Chatfilter.Cursed-Words.Match-for-action-in-Percentage");
			String[] msg = message.split(" ");
			for(String m : msg) {
				if(cursed.get(i).toLowerCase().contains(m.toLowerCase())) {
					double total = cursed.get(i).length();
					double score = m.length();
					float percentage = (float) ((score * 100) / total);
					if(percentage > minpercent) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@EventHandler
	public void onReport(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		
		if(!e.getMessage().startsWith("/report templates")) {
			if(e.getMessage().startsWith("/report ")) {
				if(spam.containsKey(p)) {
					if(spam.get(p) > System.currentTimeMillis()) {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.AntiSpam"));
						e.setCancelled(true);
					}
				}
			}
			
			if(e.getMessage().startsWith("/ticket create")) {
				if(spam.containsKey(p)) {
					if(spam.get(p) > System.currentTimeMillis()) {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ticket-System.Antispam"));
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
}
