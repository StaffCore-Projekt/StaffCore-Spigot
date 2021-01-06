package de.lacodev.rsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.ReportManager;

public class CMD_Warn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Warn.Use"))) {
				
				if(args.length >= 2) {
					
					Player target = Bukkit.getPlayer(args[0]);
					
					if(target != null) {
						
						if(target != p) {
							
							new BukkitRunnable() {

								@Override
								public void run() {
									
									String listString = "";
									for(int i = 1; i < args.length; i++) {
										listString += args[i] + " ";
									}
									
									BanManager.warnPlayer(target, p.getUniqueId().toString(), listString);
									
									ReportManager.sendNotify("WARN", p.getName(), target.getName(), listString);
									
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Created"));
								}
								
							}.runTaskAsynchronously(Main.getInstance());
							
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Cant-Warn-Self"));
						}
						
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Target-Offline"));
					}
					
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Usage"));
				}
				
			} else {
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Warn.Use")));
            }
			
		} else {
			if(args.length >= 2) {
				
				Player target = Bukkit.getPlayer(args[0]);
				
				if(target != null) {
						
					new BukkitRunnable() {

						@Override
						public void run() {
							
							String listString = "";
							for(int i = 1; i < args.length; i++) {
								listString += args[i] + " ";
							}
							
							BanManager.warnPlayer(target, "Console", listString);
							
							ReportManager.sendNotify("WARN", "Console", target.getName(), listString);
							
							sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Created"));
						}
						
					}.runTaskAsynchronously(Main.getInstance());
					
				} else {
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Target-Offline"));
				}
				
			} else {
				sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Usage"));
			}
		}
		return true;
	}

}
