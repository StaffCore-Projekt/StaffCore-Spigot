package de.lacodev.rsystem.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class ReportReasonDeleteEvent extends Event {
    private HandlerList handlers = new HandlerList();
    private String reason;
    private String senderName;

    public ReportReasonDeleteEvent(String reason, String senderName) {
        this.reason = reason;
        this.senderName = senderName;
    }

    /**
     * @return Name of the Person who deleted the reason
     * */
    public String getSenderName() {
        return senderName;
    }

    /**
     * @return the Reason witch is deleted
     * */
    public String getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
