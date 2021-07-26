package de.lacodev.rsystem.completer;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.objects.BugReport;
import de.lacodev.rsystem.utils.BugManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Completer_Bug implements TabCompleter {

    private final StaffCore staffCore;

    public Completer_Bug(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("bug").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        ArrayList<String> complete = new ArrayList<>();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Bugs.See")) ||
                        p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                    complete.add("list");
                }
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Bugs.Remove")) ||
                        p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                    complete.add("remove");
                }
                complete.add("<Your Report>");
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("list")) {
                    return complete;
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    ArrayList<BugReport> reports = staffCore.getStaffCoreLoader().getBugManager().getBugs();
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
                    ArrayList<BugReport> reports = staffCore.getStaffCoreLoader().getBugManager().getBugs();
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
