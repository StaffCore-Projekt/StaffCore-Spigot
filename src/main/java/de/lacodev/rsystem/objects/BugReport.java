package de.lacodev.rsystem.objects;

import java.util.UUID;

public class BugReport {

    private int id;
    private String playerName;
    private UUID playerUUID;
    private String bugReport;

    public BugReport(int id, String playerName, UUID playerUUID, String bugReport) {
        this.id = id;
        this.playerName = playerName;
        this.playerUUID = playerUUID;
        this.bugReport = bugReport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getBugReport() {
        return bugReport;
    }

    public void setBugReport(String bugReport) {
        this.bugReport = bugReport;
    }


}
