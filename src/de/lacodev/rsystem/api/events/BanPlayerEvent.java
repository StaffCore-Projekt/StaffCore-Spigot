package de.lacodev.rsystem.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class BanPlayerEvent extends Event {

    private Player target;
    private String reason;
    private String team;
    public BanPlayerEvent(Player target, String reason, String team) {
        this.team = team;
        this.reason = reason;
        this.target = target;
    }

    /**
     * @return the Player who got warned
     * */
    public Player getTarget() {
        return target;
    }

    /**
     * @return the reason
     * */
    public String getReason() {
        return reason;
    }

    /**
     * @return The UUID of the player who banned the target
     * */
    public String getTeam() {
        return team;
    }

    private HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
