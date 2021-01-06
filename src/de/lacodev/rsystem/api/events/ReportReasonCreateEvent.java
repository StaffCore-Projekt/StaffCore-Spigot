package de.lacodev.rsystem.api.events;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class ReportReasonCreateEvent extends Event {

    private HandlerList handlers = new HandlerList();
    private String reason;
    private Material reportItem;
    private String senderName;

    public ReportReasonCreateEvent(String reason, Material reportItem, String senderName) {
        this.reason = reason;
        this.reportItem = reportItem;
        this.senderName = senderName;
    }
    /**
     *
     * @return the Reason
     * */
    public String getReason() {
        return reason;
    }

    /**
     * @return The Name of the Player who created the reason
     * */
    public String getSenderName() {
        return senderName;
    }

    /**
     * @return The ItemStack of the Reason
     * */
    public Material getReportItem() {
        return reportItem;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
