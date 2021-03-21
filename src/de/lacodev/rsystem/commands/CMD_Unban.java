package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.rsystem.utils.SystemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Unban implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {

      Player p = (Player) sender;

      if (MySQL.isConnected()) {
        if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p
            .hasPermission(Main.getPermissionNotice("Permissions.UnBan.Use"))) {
          if (args.length == 1) {
            if (SystemManager.getUUIDByName(args[0]) != null) {
              if (BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
                if (BanManager.unban(SystemManager.getUUIDByName(args[0]))) {
                  ReportManager.sendNotify("UNBAN", p.getName(), args[0], null);
                  p.sendMessage(
                      Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Success"));
                } else {
                  p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Error"));
                }
              } else {
                p.sendMessage(
                    Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Not-Banned"));
              }
            } else {
              p.sendMessage(
                  Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Cannot-find-player"));
            }
          } else {
            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Usage"));
          }
        } else {
          p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
              .replace("%permission%", Main.getPermissionNotice("Permissions.UnBan.Use")));
        }
      } else {
        p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
      }

    } else {
      if (MySQL.isConnected()) {
        if (args.length == 1) {
          if (SystemManager.getUUIDByName(args[0]) != null) {
            if (BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
              if (BanManager.unban(SystemManager.getUUIDByName(args[0]))) {
                ReportManager.sendNotify("UNBAN", "Console", args[0], null);
                sender.sendMessage(
                    Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Success"));
              } else {
                sender
                    .sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Error"));
              }
            } else {
              sender.sendMessage(
                  Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Not-Banned"));
            }
          } else {
            sender.sendMessage(
                Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Cannot-find-player"));
          }
        } else {
          sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Usage"));
        }
      } else {
        sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
      }
    }
    return true;
  }

}
