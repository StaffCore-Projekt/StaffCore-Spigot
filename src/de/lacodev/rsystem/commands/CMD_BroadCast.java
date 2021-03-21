package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_BroadCast implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player) sender;
      if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p
          .hasPermission(Main.getPermissionNotice("Permissions.BroadCast.Send"))) {
        String message = "";
        for (String s : args) {
          message = message + " " + s;
        }

        message = ChatColor.translateAlternateColorCodes('&', "" + message);

        Bukkit.broadcastMessage(Main.getPrefix() + ChatColor.GRAY + message);

      } else {
        p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
            .replace("%permission%", Main.getPermissionNotice("Permissions.BroadCast.Send")));
      }
    }
    return false;
  }
}
