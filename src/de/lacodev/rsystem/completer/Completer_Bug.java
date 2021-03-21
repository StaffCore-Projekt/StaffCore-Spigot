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
    System.out.println("1");
    ArrayList<String> complete = new ArrayList<>();
    if (sender instanceof Player) {
      Player p = (Player) sender;
      System.out.println("2");
      if (args.length == 1) {
        System.out.println("3");
        if (p.hasPermission(Main.getPermissionNotice("Permissions.Bugs.See")) ||
            p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          complete.add("list");
        }
        System.out.println("4");
        if (p.hasPermission(Main.getPermissionNotice("Permissions.Bugs.Remove")) ||
            p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          complete.add("remove");
        }
        System.out.println("5");
        complete.add("<Your Report>");
        System.out.println("6");
      }
      System.out.println("7");
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
