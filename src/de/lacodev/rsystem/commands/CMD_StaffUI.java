package de.lacodev.rsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_StaffUI implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("verify")) {
					String uuid = args[1];
					if(!SystemManager.isVerified(uuid)) {
						MySQL.update("UPDATE staffcoreui_accounts SET UUID = '"+ uuid +"' WHERE PLAYER_NAME = '"+ p.getName() +"'");
						MySQL.update("DELETE FROM staffcoreui_sync WHERE EXECUTOR_UUID = '"+ SystemManager.getUsernameByUUID(uuid) +"' AND SYNC_TYPE = 'VERIFY_WEBACCOUNT'");
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Verification.Success"));
					}
				}
			}
			
		}
		
		return true;
	}

}
