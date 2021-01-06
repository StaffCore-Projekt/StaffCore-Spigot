package de.lacodev.rsystem.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.rsystem.utils.SystemManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CMD_CheckAlts implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.CheckAlts.Use"))) {
				
				if(args.length == 1) {
					
					String target = args[0];
					
					if(SystemManager.existsPlayerData(SystemManager.getUUIDByName(target))) {
						
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Altaccount-Check.Collecting-Data").replace("%ip%", SystemManager.getLastKnownIP(SystemManager.getUUIDByName(target))));
						
						new BukkitRunnable() {

							@SuppressWarnings("deprecation")
							@Override
							public void run() {
								ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_playerdb WHERE LAST_KNOWN_IP LIKE '%"+ SystemManager.getLastKnownIP(SystemManager.getUUIDByName(target)) +"%'");
								
								try {
									if(rs != null) {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Altaccount-Check.Possible-Alts-Title").replace("%target%", target));
										while(rs.next()) {
											if(!rs.getString("PLAYERNAME").equalsIgnoreCase(target)) {
												p.sendMessage("");
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Altaccount-Check.Prefix.Username") + rs.getString("PLAYERNAME"));
												TextComponent tc = new TextComponent();
												tc.setText(Main.getPrefix() + Main.getMSG("Messages.Altaccount-Check.More-Info-Button"));
															
												String listString = Main.getMSG("Messages.Altaccount-Check.Prefix.Last-Online") + SystemManager.getLastOnline(rs.getString("UUID")) + "\n" + Main.getMSG("Messages.Altaccount-Check.Prefix.Reports") + ReportManager.getReports(rs.getString("UUID"))
														+ "\n" + Main.getMSG("Messages.Altaccount-Check.Prefix.Bans") + BanManager.getBans(rs.getString("UUID")) + "\n" + Main.getMSG("Messages.Altaccount-Check.Prefix.Mutes") + BanManager.getMutes(rs.getString("UUID")) + "\n" + Main.getMSG("Messages.Altaccount-Check.Prefix.Warns") + BanManager.getWarns(rs.getString("UUID"))
																+ "\n \n" + Main.getMSG("Messages.Altaccount-Check.Prefix.Banned") + getBanningState(rs.getString("UUID")) + "\n" + Main.getMSG("Messages.Altaccount-Check.Prefix.Muted") + getMutingState(rs.getString("UUID"));
											
												tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(listString).create()));
												
												p.spigot().sendMessage(tc);
											}
										}
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Altaccount-Check.Nothing-found"));
									}
								} catch (SQLException e) {
									Bukkit.getConsoleSender().sendMessage("§cSystem §8(§7MySQL§8) §8- §cERROR while collecting data from MySQL-Table");
						        	Bukkit.getConsoleSender().sendMessage("§cSystem §8(§7MySQL§8) §8- §8(§7Query: §eCheckAlts§8)");
								}
							}
							
						}.runTaskLaterAsynchronously(Main.getInstance(), 3 * 20);
						
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Altaccount-Check.Player-Not-Found"));
					}
					
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Altaccount-Check.Usage"));
				}
				
			} else {
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.CheckAlts.Use")));
            }
			
		}
		return true;
	}
	
	private String getMutingState(String uuidByName) {
		if(BanManager.isMuted(uuidByName)) {
			return Main.getMSG("Messages.Altaccount-Check.State.Muted") + BanManager.getMuteReason(uuidByName);
		} else {
			return Main.getMSG("Messages.Altaccount-Check.State.No-Entry");
		}
	}

	private String getBanningState(String uuidByName) {
		if(BanManager.isBanned(uuidByName)) {
			return Main.getMSG("Messages.Altaccount-Check.State.Banned") + BanManager.getBanReason(uuidByName);
		} else {
			return Main.getMSG("Messages.Altaccount-Check.State.No-Entry");
		}
	}

}
