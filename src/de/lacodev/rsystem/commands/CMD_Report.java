package de.lacodev.rsystem.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.listeners.Listener_Chat;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.ReportManager;

public class CMD_Report implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(MySQL.isConnected()) {
				if(args.length == 0) {
					sendHelp(p);
				} else if(args.length == 1) {
					if(args[0].toLowerCase().equalsIgnoreCase("templates")) {
						p.sendMessage("");
						p.sendMessage(Main.getPrefix() + ChatColor.YELLOW + "Report Templates");
						p.sendMessage("");
						ArrayList<String> reasons = ReportManager.getReportReasons();
						
						if(reasons.size() > 0) {
							for(int i = 0; i < reasons.size(); i++) {
								p.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons.get(i));
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.No-Reportreasons"));
						}
						p.sendMessage("");
					} else {
						Player target = Bukkit.getPlayer(args[0]);
						
						if(target != null) {
							if(target != p) {
								ReportManager.openPagedReportInv(p, target.getName(), 1);
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Cannot-report-self"));
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Target-offline"));
						}
					}
				} else if(args.length == 2) {
					if(args[0].toLowerCase().equalsIgnoreCase("claim")) {

						if(p.hasPermission(Main.getPermissionNotice("Permissions.Report.Claim")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
							ReportManager.claimReport(p, args[1]);
						} else {
			                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Report.Claim")));
			            }
						
					} else {
						ArrayList<String> reasons = ReportManager.getReportReasons();
						
						if(reasons.contains(args[1].toLowerCase())) {
							Player target = Bukkit.getPlayer(args[0]);
							
							if(target != null) {
								if(target != p) {
									ReportManager.createReport(p.getName(), target, args[1]);
									if(!p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || !p.hasPermission(Main.getPermissionNotice("Permissions.Report.Spam.Bypass"))) {
										Listener_Chat.spam.put(p, System.currentTimeMillis() + Main.getInstance().getConfig().getInt("Report-Spam.Duration-in-Seconds") * 1000);
									}
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Cannot-report-self"));
								}
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Target-offline"));
							}
						} else {
							sendHelp(p);
						}
					}
				} else {
					sendHelp(p);
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
			}
			
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Console-Input" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
		}
		return true;
	}
	
	public static void sendHelp(Player p) {
		p.sendMessage("");
		p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Usage.Title"));
		p.sendMessage("");
		p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Usage.Command"));
		p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Usage.Inventory"));
		p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Usage.Template"));
		p.sendMessage("");
	}

}
