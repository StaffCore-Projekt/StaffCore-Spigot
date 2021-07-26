package de.lacodev.rsystem.completer;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.mysql.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Completer_Mute implements TabCompleter {

    private final StaffCore staffCore;

    public Completer_Mute(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("mute").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {

        if (args.length == 2) {

            ArrayList<String> reasons = new ArrayList<>();

            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'MUTE'");

            try {

                while (rs.next()) {

                    reasons.add(rs.getString("NAME").toLowerCase());

                }

                return reasons.stream().filter(a -> a.startsWith(args[args.length - 1].toLowerCase()))
                        .collect(Collectors.toList());

            } catch (SQLException e) {

                e.printStackTrace();

            }

        } else {

            return null;

        }

        return null;

    }

}
