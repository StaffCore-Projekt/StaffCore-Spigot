package de.lacodev.rsystem.completer;

import de.lacodev.rsystem.utils.ReportManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public class Completer_Report implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {

        if (args.length == 2) {
            return ReportManager.getReportReasons().stream()
                    .filter(a -> a.startsWith(args[args.length - 1].toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            return null;
        }

    }

}