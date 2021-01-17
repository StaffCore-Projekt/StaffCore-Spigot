package de.lacodev.rsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_Check implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(MySQL.isConnected()) {
				if( p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Check.Use")) ) {
					
					if( args.length == 1 ) {
						
						if( SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0])) ) {
							
							new BukkitRunnable() {

								@Override
								public void run() {
									p.sendMessage("");
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Title"));
									p.sendMessage("");
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Name") + args[0]);
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Last-IP") + SystemManager.getLastKnownIP(SystemManager.getUUIDByName(args[0])));
									p.sendMessage("");
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Reports") + ReportManager.getReports(SystemManager.getUUIDByName(args[0])));
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Bans") + BanManager.getBans(SystemManager.getUUIDByName(args[0])));
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Mutes") + BanManager.getMutes(SystemManager.getUUIDByName(args[0])));
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Warns") + BanManager.getWarns(SystemManager.getUUIDByName(args[0])));
									p.sendMessage("");
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Banned") + getBanningState(SystemManager.getUUIDByName(args[0])));
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Muted") + getMutingState(SystemManager.getUUIDByName(args[0])));
									p.sendMessage("");
								}	
							}.runTaskAsynchronously(Main.getInstance());
							
						} else {
							if(Main.getInstance().actionbar) {
								ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Player-Check.No-Player-Found"));
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.No-Player-Found"));
							}
						}
						
					} else {
						if(Main.getInstance().actionbar) {
							ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Player-Check.Usage"));
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Usage"));
						}
					}
				} else {
	                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Check.Use")));
	            }
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
			}
		} else {
			if(MySQL.isConnected()) {
				if( args.length == 1 ) {

					if( SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0])) ) {

						new BukkitRunnable() {

							@Override
							public void run() {
								sender.sendMessage("");
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Title"));
								sender.sendMessage("");
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Name") + args[0]);
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Last-IP") + SystemManager.getLastKnownIP(SystemManager.getUUIDByName(args[0])));
								sender.sendMessage("");
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Reports") + ReportManager.getReports(SystemManager.getUUIDByName(args[0])));
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Bans") + BanManager.getBans(SystemManager.getUUIDByName(args[0])));
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Mutes") + BanManager.getMutes(SystemManager.getUUIDByName(args[0])));
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Warns") + BanManager.getWarns(SystemManager.getUUIDByName(args[0])));
								sender.sendMessage("");
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Banned") + getBanningState(SystemManager.getUUIDByName(args[0])));
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Prefix.Muted") + getMutingState(SystemManager.getUUIDByName(args[0])));
								sender.sendMessage("");
							}
						}.runTaskAsynchronously(Main.getInstance());

					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.No-Player-Found"));
					}

				} else {
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Player-Check.Usage"));
				}
			} else {
				sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
			}
		}
		return true;
	}

	private String getMutingState(String uuidByName) {
		if(BanManager.isMuted(uuidByName)) {
			return Main.getMSG("Messages.Player-Check.State.Muted") + BanManager.getMuteReason(uuidByName);
		} else {
			return Main.getMSG("Messages.Player-Check.State.No-Entry");
		}
	}

	private String getBanningState(String uuidByName) {
		if(BanManager.isBanned(uuidByName)) {
			return Main.getMSG("Messages.Player-Check.State.Banned") + BanManager.getBanReason(uuidByName);
		} else {
			return Main.getMSG("Messages.Player-Check.State.No-Entry");
		}
	}

}
