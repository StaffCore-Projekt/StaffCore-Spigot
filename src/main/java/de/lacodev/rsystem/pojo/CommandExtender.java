package de.lacodev.rsystem.pojo;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.command.CommandExecutor;

public abstract class CommandExtender implements CommandExecutor {

    private final StaffCore staffCore;

    public CommandExtender(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    public String getMessage(String message){
        return this.staffCore.getStaffCoreLoader().getMessage(message);
    }

    public String getPermission(String permission){
        return this.staffCore.getStaffCoreLoader().getPermission(permission);
    }
}
