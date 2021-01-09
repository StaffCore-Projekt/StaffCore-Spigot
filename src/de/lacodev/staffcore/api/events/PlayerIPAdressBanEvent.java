package de.lacodev.staffcore.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerIPAdressBanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private String teamUsername;

    private Player target;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerIPAdressBanEvent(String teamUsername, Player target) {
        this.teamUsername = teamUsername;
        this.target = target;
    }

    public String getTeamUsername() {
        return this.teamUsername;
    }

    public Player getTarget() {
        return this.target;
    }
}
