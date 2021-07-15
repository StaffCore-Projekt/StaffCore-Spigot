package de.lacodev.staffcore.api;

import de.lacodev.rsystem.utils.BanManager;
import org.bukkit.entity.Player;

public class WarnOptions {

    public int getWarns(String uuid) {
        return BanManager.getWarns(uuid);
    }

    public void warnPlayer(Player t, String teamUuid, String reason) {
        BanManager.warnPlayer(t, teamUuid, reason);
    }
}
