package de.lacodev.rsystem.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MuteReasonDeleteEvent extends Event {
    private HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private String reason;

    public MuteReasonDeleteEvent(String reason) {
        this.reason = reason;
    }

    /**
     * @return the Reason Name
     * */
    public String getReason() {
        return reason;
    }
}
