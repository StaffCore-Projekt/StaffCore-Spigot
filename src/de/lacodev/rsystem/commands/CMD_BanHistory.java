package de.lacodev.rsystem.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_BanHistory implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.BanHistory.See"))) {
				if(MySQL.isConnected()) {
					if(args.length == 1) {
						if(SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {
							new BukkitRunnable() {

								@Override
								public void run() {
									ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_banhistory WHERE BANNED_UUID = '"+ SystemManager.getUUIDByName(args[0]) +"'");
									try {
										if(rs.next()) {
											p.sendMessage("");
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Title") + args[0]);
											p.sendMessage("");
											if(rs.getFetchSize() > 10) {
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.History-too-long"));
											}
											rs.setFetchSize(10);
											while(rs.next()) {
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Prefix.BanID") + rs.getInt("id"));
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Prefix.Reason") + rs.getString("REASON"));
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Prefix.BannedBy") + SystemManager.getUsernameByUUID(rs.getString("TEAM_UUID")));
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Prefix.BannedSince") + new Date(rs.getLong("BAN_START")) + " " + checkForExpiration(rs.getString("BANNED_UUID"), rs.getString("REASON"), rs.getLong("BAN_END")));
												p.sendMessage("");
											}
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.No-History-Available"));
										}
										p.sendMessage("");
									} catch (SQLException e) {
										Bukkit.getConsoleSender().sendMessage("");
										Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Fetch BanHistory" + ChatColor.DARK_GRAY + ")");
										Bukkit.getConsoleSender().sendMessage("");
									}
								}					
							}.runTaskAsynchronously(Main.getInstance());
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Target-Never-Joined"));
						}
					} else if(args.length == 2) {
						if(SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {
							if(args[1].toLowerCase().equalsIgnoreCase("clear")) {
								new BukkitRunnable() {

									@Override
									public void run() {
										MySQL.update("DELETE FROM ReportSystem_banhistory WHERE BANNED_UUID = '"+ SystemManager.getUUIDByName(args[0]) +"'");
									}
									
								}.runTaskAsynchronously(Main.getInstance());
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Cleared"));
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Target-Never-Joined"));
						}
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Usage"));
					}
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.BanHistory.See")));
			}
		} else {
			if(MySQL.isConnected()) {
				if(args.length == 1) {
					if(SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {
						new BukkitRunnable() {

							@Override
							public void run() {
								ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_banhistory WHERE BANNED_UUID = '"+ SystemManager.getUUIDByName(args[0]) +"'");
								try {
									if(rs.next()) {
										sender.sendMessage("");
										sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Title") + args[0]);
										sender.sendMessage("");
										if(rs.getFetchSize() > 10) {
											sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.History-too-long"));
										}
										rs.setFetchSize(10);
										while(rs.next()) {
											sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Prefix.BanID") + rs.getInt("id"));
											sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Prefix.Reason") + rs.getString("REASON"));
											sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Prefix.BannedBy") + SystemManager.getUsernameByUUID(rs.getString("TEAM_UUID")));
											sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Prefix.BannedSince") + new Date(rs.getLong("BAN_START")) + " " + checkForExpiration(rs.getString("BANNED_UUID"), rs.getString("REASON"), rs.getLong("BAN_END")));
											sender.sendMessage("");
										}
									} else {
										sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.No-History-Available"));
									}
									sender.sendMessage("");
								} catch (SQLException e) {
									Bukkit.getConsoleSender().sendMessage("");
									Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Fetch BanHistory" + ChatColor.DARK_GRAY + ")");
									Bukkit.getConsoleSender().sendMessage("");
								}
							}					
						}.runTaskAsynchronously(Main.getInstance());
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Target-Never-Joined"));
					}
				} else if(args.length == 2) {
					if(SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {
						if(args[1].toLowerCase().equalsIgnoreCase("clear")) {
							new BukkitRunnable() {

								@Override
								public void run() {
									MySQL.update("DELETE FROM ReportSystem_banhistory WHERE BANNED_UUID = '"+ SystemManager.getUUIDByName(args[0]) +"'");
								}
								
							}.runTaskAsynchronously(Main.getInstance());
							sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Cleared"));
						}
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Target-Never-Joined"));
					}
				} else {
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.History.Usage"));
				}
			} else {
				sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
			}
		}
		return false;
	}

	protected String checkForExpiration(String uuid, String reason, long long1) {
		if(System.currentTimeMillis() > long1) {
			return ChatColor.DARK_GRAY + "(" + ChatColor.RED + "Expired" + ChatColor.DARK_GRAY + ")";
		} else {
			if(BanManager.isBanned(uuid)) {
				if(BanManager.getBanReason(uuid).matches(reason)) {
					if(BanManager.getBanEnd(uuid) == -1) {
						return ChatColor.DARK_GRAY + "("+ Main.getMSG("Layout.Ban.Length-Values.Permanently") +ChatColor.DARK_GRAY + ")";
					} else {
						return ChatColor.DARK_GRAY + "("+ BanManager.getBanFinalEnd(uuid) + ChatColor.DARK_GRAY + ")";
					}
				} else {
					return Main.getMSG("Messages.Ban-System.History.Status.Unbanned");
				}
			} else {
				return Main.getMSG("Messages.Ban-System.History.Status.Unbanned");
			}
		}
	}
}
