package de.lacodev.staffcore.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportCreateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private String reporter;

    private Player target;

    private String reason;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ReportCreateEvent(String reporter, Player target, String reason) {
        this.reporter = reporter;
        this.target = target;
        this.reason = reason;
    }

    public String getReporter() {
        return this.reporter;
    }

    public Player getTarget() {
        return this.target;
    }

    public String getReason() {
        return this.reason;
    }
}
