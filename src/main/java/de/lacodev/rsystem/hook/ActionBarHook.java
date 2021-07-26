package de.lacodev.rsystem.hook;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.StaffCore;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@Getter
public class ActionBarHook {
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;
    private ActionBarAPI actionBarAPI;
    private boolean enabled;

    public ActionBarHook(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.enabled = false;
    }

    public void init(){
        if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("General.Include-ActionBarAPI")){
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.GRAY + " Try To hook the ActionBarAPI...");
            Bukkit.getConsoleSender().sendMessage("");
            if (Bukkit.getPluginManager().getPlugin("ActionBarAPI") != null){
                this.actionBarAPI = (ActionBarAPI) Bukkit.getPluginManager().getPlugin("ActionBarAPI");
                this.enabled = true;
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added ActionBarAPI to our system");
            }else{
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add ActionBarAPI to our system! Please make sure you have ActionBarAPI installed!");
            }
            Bukkit.getConsoleSender().sendMessage("");


        }
    }
}
