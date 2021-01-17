package de.lacodev.rsystem.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class CMD_Warns implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			


			if(args.length == 1) {
				if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Warns.See"))) {
					if (SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {

						String target = args[0];

						TextComponent w = new TextComponent();
						w.setText(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warns.Player-Info").replace("%target%", target).replace("%warns%", "" + BanManager.getWarns(SystemManager.getUUIDByName(target))));

						String listString = "\n";
						for (String s : getWarnReasonsFromPlayer(SystemManager.getUUIDByName(target))) {
							listString += ChatColor.DARK_GRAY + "- " + ChatColor.RED + s + "\n";
						}

						w.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(listString).create()));

						p.spigot().sendMessage(w);

						TextComponent tc = new TextComponent();
						tc.setText(" " + Main.getMSG("Messages.Warn-System.Warns.Action-Ban-Button"));
						tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/ban " + target));

						TextComponent tc1 = new TextComponent();
						tc1.setText(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warns.Action-Mute-Button"));
						tc1.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/mute " + target));
						tc1.addExtra(tc);

						p.spigot().sendMessage(tc1);

					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warns.Never-Joined"));
					}
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Warns.See")));
				}
			} else if ((args.length == 2) && args[1].equalsIgnoreCase("clear")){

				if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Warns.Clear"))) {
					if (SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {

						BanManager.clearWans(SystemManager.getUUIDByName(args[0]));

						//PREFIX + "You Cleared the "
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Clear").replace("%player%", args[0]));

					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warns.Never-Joined"));
					}
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Warns.Clear")));
				}
			}else{
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warns.Usage"));
			}


			
		} else {
			if(args.length == 1) {
				
				if(SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {
					
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warns.Player-Info").replace("%target%", args[0]).replace("%warns%", "" + BanManager.getWarns(SystemManager.getUUIDByName(args[0]))));
					
				} else {
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warns.Never-Joined"));
				}
				
			} else {
				sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warns.Usage"));
			}
		}
		return true;
	}
	
	private ArrayList<String> getWarnReasonsFromPlayer(String uuid) {
		ArrayList<String> r = new ArrayList<>();
		
		ResultSet rs = MySQL.getResult("SELECT REASON FROM ReportSystem_warnsdb WHERE UUID = '"+ uuid +"'");
		try {
			while(rs.next()) {
				r.add(rs.getString("REASON"));
			}
			return r;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}

}
