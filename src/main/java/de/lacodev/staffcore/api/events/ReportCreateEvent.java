package de.lacodev.staffcore.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportCreateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String reporter;

    private final Player target;

    private final String reason;

    public ReportCreateEvent(String reporter, Player target, String reason) {
        this.reporter = reporter;
        this.target = target;
        this.reason = reason;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
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
