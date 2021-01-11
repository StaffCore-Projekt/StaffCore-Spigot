package de.lacodev.rsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.InventoryHandler;

public class CMD_ChatFilter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Chatfilter.Manage")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
				if(args.length == 0) {
					InventoryHandler.openChatFilterSettings(p);
				} else {
					p.sendMessage(Main.getPrefix() + ChatColor.RED + "Too many arguments");
				}
			} else {
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Chatfilter.Manage")));
            }
			
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Console-Input" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
		}
		return true;
	}

}
