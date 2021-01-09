package de.lacodev.staffcore.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportClaimEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private Player teamUuid;

    private String targetuuid;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ReportClaimEvent(Player teamUuid, String targetuuid) {
        this.teamUuid = teamUuid;
        this.targetuuid = targetuuid;
    }

    public Player getTeamUuid() {
        return this.teamUuid;
    }

    public String getTargetuuid() {
        return this.targetuuid;
    }
}
