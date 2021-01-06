package de.lacodev.rsystem.api.events;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class BanReasonDeleteEvent extends Event {
    private HandlerList handlers = new HandlerList();

    private String reason;

    public BanReasonDeleteEvent(String reason) {
        this.reason = reason;
    }

    /**
     * @return the reason
     * */
    public String getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
