package de.lacodev.rsystem.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class WarnPlayerEvent extends Event {
    private HandlerList handlers = new HandlerList();

    private Player target;
    private String warnerUUID;
    private String warnerName;
    private String reason;

    public WarnPlayerEvent(Player target, String warnerUUID, String warnerName, String reason) {
        this.warnerName = warnerName;
        this.target = target;
        this.warnerUUID = warnerUUID;
        this.reason = reason;
    }

    /**
     * @return the warner Name
     * */
    public String getWarnerName() {
        return warnerName;
    }

    /**
     * @return the Target who got warned
     * */
    public Player getTarget() {
        return target;
    }

    /**
     * @return the Warner UUID
     * */
    public String getWarnerUUID() {
        return warnerUUID;
    }

    /**
     * @return the Reason for what the Target got warned
     * */
    public String getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
