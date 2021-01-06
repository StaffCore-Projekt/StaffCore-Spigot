package de.lacodev.rsystem.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.TicketManager;

public class CMD_Ticket implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(args.length == 0) {
				p.sendMessage(Main.getPrefix() + "§7/ticket create");
				p.sendMessage(Main.getPrefix() + "§7/ticket list");
			}
			
			if(args.length == 1) {
				
				if(args[0].equalsIgnoreCase("create")) {
					
					TicketManager.createTicket(p);
					
				}
				
				if(args[0].equalsIgnoreCase("list")) {
					
					if(TicketManager.hasTicket(p)) {
						
						try {
							
							TicketManager.openTicketOverview(p, 1);
							
						} catch (SQLException e) {
							
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
							
						}
						
					}
					
				}
				
			}
			
		}
		
		return true;
	}

}
