package de.lacodev.staffcore.api;

import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public void freezePlayer(Player t) {
        BanManager.freeze(t);
    }

    public void unfreezePlayer(Player t) {
        BanManager.unfreeze(t);
    }

    public boolean existsPlayerData(String uuid) {
        return SystemManager.existsPlayerData(uuid);
    }

    public String getLastOnline(String uuid) {
        if (existsPlayerData(uuid)) {
            return SystemManager.getLastOnline(uuid);
        }
        return "unknown";
    }

    public String getLastIP(String uuid) {
        if (existsPlayerData(uuid)) {
            return SystemManager.getLastKnownIP(uuid);
        }
        return "unknown";
    }

    public boolean isProtected(String uuid) {
        if (existsPlayerData(uuid)) {
            return SystemManager.isProtected(uuid);
        }
        return false;
    }

    public String getUuidByName(String name) {
        return SystemManager.getUUIDByName(name);
    }

    public String getNameByUuid(String uuid) {
        return SystemManager.getUsernameByUUID(uuid);
    }
}
