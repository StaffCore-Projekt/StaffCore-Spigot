package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffUIComamnd implements CommandExecutor {

    private final StaffCore staffCore;

    public StaffUIComamnd(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("staffui").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("verify")) {
                    String uuid = args[1];
                    if (!staffCore.getStaffCoreLoader().getSystemManager().isVerified(uuid)) {
                        staffCore.getStaffCoreLoader().getMySQL().update(
                                "UPDATE staffcoreui_accounts SET UUID = '" + uuid + "' WHERE PLAYER_NAME = '" + p
                                        .getName() + "'");
                        staffCore.getStaffCoreLoader().getMySQL().update("DELETE FROM staffcoreui_sync WHERE EXECUTOR_UUID = '" + staffCore.getStaffCoreLoader().getSystemManager()
                                .getUsernameByUUID(uuid) + "' AND SYNC_TYPE = 'VERIFY_WEBACCOUNT'");
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Verification.Success"));
                    }
                }
            }

        }

        return true;
    }

}
