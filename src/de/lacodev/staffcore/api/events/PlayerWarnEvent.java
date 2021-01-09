package de.lacodev.staffcore.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWarnEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private Player target;

    private String warner;

    private String reason;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerWarnEvent(Player target, String warner, String reason) {
        this.target = target;
        this.warner = warner;
        this.reason = reason;
    }

    public Player getTarget() {
        return this.target;
    }

    public String getWarner() {
        return this.warner;
    }

    public String getReason() {
        return this.reason;
    }
}
