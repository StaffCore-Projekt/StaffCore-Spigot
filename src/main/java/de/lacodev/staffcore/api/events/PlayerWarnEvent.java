package de.lacodev.staffcore.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWarnEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player target;

    private final String warner;

    private final String reason;

    public PlayerWarnEvent(Player target, String warner, String reason) {
        this.target = target;
        this.warner = warner;
        this.reason = reason;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
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