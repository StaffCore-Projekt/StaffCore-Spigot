package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.mysql.MySQL;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatLogListener implements Listener {

    private final StaffCore staffCore;

    public ChatLogListener(StaffCore staffCore) {
        this.staffCore = staffCore;
        Bukkit.getPluginManager().registerEvents(this, staffCore);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLog(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!e.isCancelled()) {
            staffCore.getStaffCoreLoader().getMySQL().update(
                    "INSERT INTO ReportSystem_messages(SENDER_UUID,MESSAGE) VALUES ('" + p.getUniqueId()
                            .toString() + "','" + e.getMessage() + "')");
        }
    }
}
