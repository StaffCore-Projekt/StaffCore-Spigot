package de.lacodev.rsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.PanelManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_StaffCore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(args.length == 1) {
				if(args[0].toLowerCase().equalsIgnoreCase("gui")) {
					if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.System.Adminpanel"))) {
						PanelManager manager = new PanelManager();
						
						manager.openMainMenu(p);
					} else {
						if(Main.getInstance().experimental == true) {
							p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§dEXPERIMENTAL§8)");
						} else if(Main.getInstance().latest == true) {
							p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§aLATEST§8)");
						} else {
							p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§cOUTDATED§8)");
							p.sendMessage(Main.getPrefix() + "§7/staffcore gui §8| §7To open the adminconsole");
						}
						p.sendMessage(Main.getPrefix() + "§8Created by: §7LacoDev and Esmaybe");
					}
				} else {
					if(Main.getInstance().experimental == true) {
						p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§dEXPERIMENTAL§8)");
					} else if(Main.getInstance().latest == true) {
						p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§aLATEST§8)");
					} else {
						p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§cOUTDATED§8)");
						p.sendMessage(Main.getPrefix() + "§7/staffcore gui §8| §7To open the adminconsole");
					}
					p.sendMessage(Main.getPrefix() + "§8Created by: §7LacoDev and Esmaybe");
				}
			} else {
				p.sendMessage("");
				if(Main.getInstance().experimental == true) {
					p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§dEXPERIMENTAL§8)");
				} else if(Main.getInstance().latest == true) {
					p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§aLATEST§8)");
				} else {
					p.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§cOUTDATED§8)");
					p.sendMessage(Main.getPrefix() + "§7/staffcore gui §8| §7To open the adminconsole");
				}
				p.sendMessage(Main.getPrefix() + "§8Created by: §7LacoDev and Esmaybe");
				p.sendMessage("");
			}
			
		} else {
			if(args.length == 1) {
				if(args[0].toLowerCase().equalsIgnoreCase("reload")) {
					
					SystemManager.reloadStaffCore(sender);
					
				} else if(args[0].toLowerCase().equalsIgnoreCase("update")) {
					
					if(!Main.getInstance().latest) {
						if(!Main.getInstance().experimental) {
							SystemManager.downloadLatestVersion(sender);
						} else {
							sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Version.Experimental"));
						}
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Version.Latest"));
					}
				}
			} else {
				sender.sendMessage("");
				if(Main.getInstance().experimental == true) {
					sender.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§dEXPERIMENTAL§8)");
				} else if(Main.getInstance().latest == true) {
					sender.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§aLATEST§8)");
				} else {
					sender.sendMessage(Main.getPrefix() + "§8StaffCore: §7v" + Main.getInstance().getDescription().getVersion() + "§8(§cOUTDATED§8)");
					sender.sendMessage(Main.getPrefix() + "§7/staffcore gui §8| §7To open the adminconsole");
				}
				sender.sendMessage(Main.getPrefix() + "§8Created by: §7LacoDev and ViaEnder");
				sender.sendMessage("");
			}
		}
		
		return true;
	}

}
