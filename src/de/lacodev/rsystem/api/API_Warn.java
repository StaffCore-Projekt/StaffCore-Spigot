package de.lacodev.rsystem.api;

import de.lacodev.rsystem.errors.PlayerIsProtectedExeption;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class API_Warn {

    public int getWarns(String uuid)  {
        return BanManager.getWarns(uuid);
    }

    public void warnPlayer(Player target, UUID warner, String reason) throws PlayerIsProtectedExeption {
        if (SystemManager.isProtected(target.getUniqueId().toString())) {
            throw new PlayerIsProtectedExeption(target.getName());
        } else {
            BanManager.warnPlayer(target, warner.toString(), reason);
        }
    }



}
