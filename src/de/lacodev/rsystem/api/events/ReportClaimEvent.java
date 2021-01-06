package de.lacodev.rsystem.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** @author ViaEnder
 * @version 1.0.0
 * */
public class ReportClaimEvent extends Event {
    private HandlerList handlers = new HandlerList();

    private Player claimer;
    private String targetUUID;

    public ReportClaimEvent(Player claimer, String targetUUID) {
        this.claimer = claimer;
        this.targetUUID = targetUUID;
    }

    /**
     *
     * @return player who claims it
     * */
    public Player getClaimer() {
        return claimer;
    }

    /**
     * @return the Target UUID
     * */
    public String getTargetUUID() {
        return targetUUID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
