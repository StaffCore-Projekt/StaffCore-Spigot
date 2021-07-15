package de.lacodev.rsystem.completer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Completer_Kick implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completer = new ArrayList<>();
        if (args.length == 1){
            Bukkit.getOnlinePlayers().forEach(x -> completer.add(x.getName()));
        }else if (args.length == 2){
            completer.add("reason");
        }

        return completer.stream().filter(a -> a.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
