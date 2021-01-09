package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private String targetuuid;

    private String reason;

    private String teamUuid;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerBanEvent(String targetuuid, String reason, String teamUuid) {
        this.targetuuid = targetuuid;
        this.reason = reason;
        this.teamUuid = teamUuid;
    }

    public String getTargetuuid() {
        return this.targetuuid;
    }

    public String getReason() {
        return this.reason;
    }

    public String getTeamUuid() {
        return this.teamUuid;
    }
}
