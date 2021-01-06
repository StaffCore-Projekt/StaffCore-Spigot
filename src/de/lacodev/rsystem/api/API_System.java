package de.lacodev.rsystem.api;

import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;
import org.bukkit.entity.Player;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class API_System {

    /**
     * To get the Playername
     *
     * @param uuid the uuid of the target
     *
     * @return target name from the System | UNKNOWN = Player not in System
     */
    public String getPlayernameByUUID(String uuid) {
        return SystemManager.getUsernameByUUID(uuid);
    }

    /**
     * To get the Player UUID from the System
     *
     * @param targetname the Player Name of the Target
     *
     * @return String With the UUID of the Player | UNKNOWN = Player not in System
     * */
    public String  getUUIDByPlayername(String targetname) {
        return SystemManager.getUUIDByName(targetname);
    }

    /**
     *
     * Check if a Player is Protected!
     * @param uuid the uuid of the target
     *
     * @return true if is Protected | false if its not
     * */
    public boolean isProtected(String uuid) {
        return SystemManager.isProtected(uuid);
    }

    /**
     * freezing the Player (Do Not Forget to unfreeze)
     *
     * @param t the Player you would like to freeze
     * */
    public void freeze(Player t){
        BanManager.freeze(t);
    }

    /**
     * unfreezing the Player
     *
     * @param t the Player you would like to unfreeze
     * */
    public void unfreeze(Player t){
        BanManager.unfreeze(t);
    }

}
