package de.lacodev.rsystem.api.events;

import de.lacodev.rsystem.utils.SystemManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class MutePlayerEvent extends Event {
    private HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private String targetUUID;
    private String reason;
    private String team;

    public MutePlayerEvent(String targetUUID, String reason, String team) {
        this.targetUUID = targetUUID;
        this.reason = reason;
        this.team = team;
    }

    /**
     * @return the UUID of the player who got muted
     * */
    public String getTargetUUID() {
        return targetUUID;
    }

    /**
     * @return the Reason
     * */
    public String getReason() {
        return reason;
    }

    /**
     * @return the UUID of the Player who muted the Player or "CONSOLE" when it is the console.
     * */
    public String getTeam() {
        if (SystemManager.existsPlayerData(team)){
            return team;
        }else {
            return "CONSOLE";
        }

    }
}
