package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class Listener_Login implements Listener {

    private final StaffCore staffCore;

    public Listener_Login(StaffCore staffCore) {
        this.staffCore = staffCore;
        Bukkit.getPluginManager().registerEvents(this, staffCore);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (staffCore.getStaffCoreLoader().getSettingsManager().isKey("maintenance")) {
            if (staffCore.getStaffCoreLoader().getSettingsManager().getBoolean("maintenance")) {
                if (!(p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Maintenance.Join")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")))) {
                    // TODO: 18.01.2021 Insert Translator
                    e.disallow(Result.KICK_FULL, "Sorry but we are currently in Maintenance!");
                    return;
                }
            }
        }

        if (staffCore.getStaffCoreLoader().getBanManager().isBanned(p.getUniqueId().toString())) {

            if (staffCore.getStaffCoreLoader().getBanManager().getBanEnd(p.getUniqueId().toString()) != -1) {
                e.disallow(Result.KICK_BANNED, staffCore.getStaffCoreLoader().getMessage("Messages.Layouts.Ban")
                        .replace("%reason%", staffCore.getStaffCoreLoader().getBanManager().getBanReason(p.getUniqueId().toString()))
                        .replace("%remaining%", staffCore.getStaffCoreLoader().getBanManager().getBanFinalEnd(p.getUniqueId().toString()))
                        .replace("%lengthvalue%",
                                staffCore.getStaffCoreLoader().getMessage("Messages.Layouts.Ban.Length-Values.Temporarly")));
            } else {
                e.disallow(Result.KICK_BANNED, staffCore.getStaffCoreLoader().getMessage("Messages.Layouts.Ban")
                        .replace("%reason%", staffCore.getStaffCoreLoader().getBanManager().getBanReason(p.getUniqueId().toString()))
                        .replace("%remaining%", staffCore.getStaffCoreLoader().getMessage("Messages.Layouts.Ban.Length-Values.Permanently"))
                        .replace("%lengthvalue%",
                                staffCore.getStaffCoreLoader().getMessage("Messages.Layouts.Ban.Length-Values.Permanently")));
            }

        } else {
            if (!staffCore.getStaffCoreLoader().getBanManager().isIPBanned(e.getRealAddress().toString())) {
                if (p.isOp()) {
                    if (!p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Allow-OP.Join"))) {
                        e.disallow(Result.KICK_OTHER,
                                staffCore.getStaffCoreLoader().getMessage("Messages.System.Unpermitted-OP.Kick-Player"));
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Allow-OP.Notify"))) {
                                all.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Unpermitted-OP.Notify")
                                                .replace("%target%", p.getName()));
                            }
                        }
                    }
                } else {
                    if (staffCore.getStaffCoreLoader().getAntiMCLeaksHandler().isAccountCached(p.getUniqueId().toString())) {
                        e.disallow(Result.KICK_OTHER,
                                staffCore.getStaffCoreLoader().getMessage("Messages.System.MCLeaks-Blocker.Blocked-Accounts.Kick"));
                    }
                }
            } else {
                e.disallow(Result.KICK_BANNED, staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-Ban.Kick-Screen"));
            }
        }
    }

}
