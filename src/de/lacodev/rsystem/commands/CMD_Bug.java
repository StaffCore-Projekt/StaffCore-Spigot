package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.objects.BugReport;
import de.lacodev.rsystem.utils.BugManager;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Bug implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player) sender;
      if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
        if (p.hasPermission(Main.getPermissionNotice("Permissions.Bugs.See")) || p
            .hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          ArrayList<BugReport> bugReports = BugManager.getBugs();
          if (bugReports == null || bugReports.size() == 0) {
            // TODO: 16.03.2021 Keine Reports
            p.sendMessage(
                Main.getPrefix() + ChatColor.GRAY + " There are no open Reports at this Moment.");
            return true;
          }
          StringBuilder message = new StringBuilder(
              Main.getPrefix() + ChatColor.GRAY + "Reported Bugs: "
                  + ChatColor.DARK_GRAY + "( " + ChatColor.GRAY + bugReports.size()
                  + ChatColor.DARK_GRAY + " )");
          for (BugReport report : bugReports) {
            message.append("\n").append(ChatColor.GRAY).append("    by ").append(ChatColor.GREEN)
                .append("%PlayerName%").append(ChatColor.GRAY).append(": ")
                .append(ChatColor.RED).append("%Report%").append(ChatColor.DARK_GRAY)
                .append(" (").append(ChatColor.GRAY).append(report.getId())
                .append(ChatColor.DARK_GRAY).append(")");
            message = new StringBuilder(message.toString()
                .replace("%PlayerName%", "" + report.getPlayerName())
                .replace("%ID%", "" + report.getId())
                .replace("%Report%", "" + report.getBugReport()));
          }
          p.sendMessage(message.toString());
        } else {
          p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
              .replace("%permission%",
                  "" + Main.getInstance().getConfig().getString("Permissions.Bugs.See")));
        }
      } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
        if (p.hasPermission(Main.getPermissionNotice("Permissions.Bugs.Remove")) || p
            .hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          try {
            int id = Integer.parseInt(args[1]);
            ArrayList<BugReport> bugReports = BugManager.getBugs();
            if (bugReports == null) {
              p.sendMessage(
                  Main.getPrefix() + ChatColor.GRAY + " There are no open Reports at this Moment.");
              return true;
              // TODO: 16.03.2021 Nachricht, das es keine Reports gibt!
            }
            BugReport bugReport = bugReports.stream().filter(bugReport1 -> bugReport1.getId() == id)
                .findFirst().orElse(null);

            if (bugReport == null) {
              // TODO: 20.03.2021 Nachricht, ID Nicht gefunden.
              p.sendMessage(
                  Main.getPrefix() + ChatColor.GRAY + " Sorry, but we can't find the ID: " + id
                      + ".");
              return true;
            }
            BugManager.deleteBug(bugReport);
          } catch (NumberFormatException ignored) {
            // TODO: 16.03.2021 Ausgabe das es eine ID sein soll!
          }
        } else {
          p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
              .replace("%permission%",
                  "" + Main.getInstance().getConfig().getString("Permissions.Bugs.Remove")));
        }
      } else {
        // TODO: 16.03.2021 Meldung Syntax
        p.sendMessage(Main.getPrefix() + ChatColor
            .translateAlternateColorCodes('&', "&cUse: &7/bug <list / remove / the BugReport>"));
      }
    }

        /*if (sender instanceof Player){
            Player p = (Player) sender;
            if (args.length == 1 && args[0].equalsIgnoreCase("list")){
                if (p.hasPermission(Main.getPermissionNotice("Permissions.Bugs.See")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))){

                    HashMap<UUID, String> bugs = BugManager.getBugs();


                    if (bugs != null) {
                        StringBuilder fullMessage = new StringBuilder("Bugs: " + bugs.size());

                        for (UUID reporter : bugs.keySet()){
                            String message = bugs.get(reporter);
                            fullMessage.append("\n  - ").append(SystemManager.getUsernameByUUID(reporter.toString())).append(": ").append(message);
                        }

                        p.sendMessage(fullMessage.toString());
                    }


                }
            }else {
                //SYNTAX:  /bug [Bug Report Message]
                String message = "";
                for (String m : args) {
                    message = message + " " + m;
                }

                BugManager.createBug(p.getUniqueId(), message);

                p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "Thanks for the Bug report, we will fix this in the future!");

                for (Player teamler : p.getServer().getOnlinePlayers()) {
                    if (teamler.hasPermission(Main.getPermissionNotice("Permissions.Bugs.Notify")) || teamler.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
                        teamler.sendMessage(Main.getPrefix() + "");
                        teamler.sendMessage(Main.getPrefix() + ChatColor.GREEN + p.getName() + ChatColor.GRAY + " reported an Bug: " + ChatColor.RED + message);
                        teamler.sendMessage(Main.getPrefix() + "");
                    }
                }
            }
        }else{

        }*/

    return false;
  }
}
