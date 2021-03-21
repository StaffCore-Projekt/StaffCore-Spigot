package de.lacodev.rsystem.completer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class Completer_Maintenance implements TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
      String[] args) {

    ArrayList<String> manager = new ArrayList<>();

    manager.add("on");
    manager.add("off");
    manager.add("info");

    if (args.length == 1) {
      return manager.stream().filter(a -> a.startsWith(args[args.length - 1].toLowerCase()))
          .collect(Collectors.toList());
    } else {
      manager.clear();
      return manager;
    }
  }
}
