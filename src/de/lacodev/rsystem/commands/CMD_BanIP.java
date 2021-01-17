package de.lacodev.rsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;

public class CMD_BanIP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.IpBan.Use"))) {
				if(args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null) {
						if(!BanManager.isIPBanned(target.getAddress().getAddress().toString())) {
							BanManager.banIPAddress(p.getName(), target);
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-Ban.Already-banned").replace("%target%", target.getName()));
						}
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-Ban.Target-Offline"));
					}
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-Ban.Usage"));
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.IpBan.Use")));
			}
		}
		return true;
	}

}
