package de.lacodev.rsystem.completer;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.objects.BugReport;
import de.lacodev.rsystem.utils.BugManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class Completer_Bug implements TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
      String[] args) {
    ArrayList<String> complete = new ArrayList<>();
    if (sender instanceof Player) {
      Player p = (Player) sender;
      if (args.length == 1) {
        if (p.hasPermission(Main.getPermissionNotice("Permissions.Bugs.See")) ||
            p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          complete.add("list");
        }
        if (p.hasPermission(Main.getPermissionNotice("Permissions.Bugs.Remove")) ||
            p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          complete.add("remove");
        }
        complete.add("<Your Report>");
      }else if (args.length == 2){
        if (args[1].equalsIgnoreCase("list")) {
          return complete;
        }
        if (args[1].equalsIgnoreCase("remove")){
          ArrayList<BugReport> reports = BugManager.getBugs();
          if (reports == null) {
            complete.add("No Reports are open!");
            return complete;
          }
          reports.forEach(bugReport -> complete.add(bugReport.getId() + ""));
        }
      }
    } else {
      if (args.length == 1) {
        complete.add("list");
        complete.add("remove");
        complete.add("<Your Report>");
      }
      if (args.length == 2) {
        if (args[1].equalsIgnoreCase("list")) {
          return complete;
        }
        if (args[1].equalsIgnoreCase("remove")) {
          ArrayList<BugReport> reports = BugManager.getBugs();
          if (reports == null) {
            complete.add("No Reports are open!");
            return complete;
          }
          reports.forEach(bugReport -> complete.add(bugReport.getId() + ""));
        }
      }
    }
    return complete.stream().filter(a -> a.startsWith(args[args.length - 1].toLowerCase())).collect(
        Collectors.toList());
  }
}
