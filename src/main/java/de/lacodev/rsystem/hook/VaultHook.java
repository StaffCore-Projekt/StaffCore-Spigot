package de.lacodev.rsystem.hook;

import de.lacodev.rsystem.StaffCore;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;

@Getter
public class VaultHook {
    private final StaffCore staffCore;

    private Economy economy;

    public VaultHook(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    public void init(){
        if(staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("General.Include-Vault")) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.GRAY + " Try To hook the Vault...");
            Bukkit.getConsoleSender().sendMessage("");
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<Economy> rsp = staffCore.getServer().getServicesManager().getRegistration(Economy.class);
                if (rsp != null) {
                    economy = rsp.getProvider();
                }
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added Vault to our system");
            }else{
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.RED + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add Vault to our system! Please make sure you have Vault installed!");
            }
            Bukkit.getConsoleSender().sendMessage("");
        }
    }

    public boolean economyEnabled(){
        return economy != null;
    }


}
