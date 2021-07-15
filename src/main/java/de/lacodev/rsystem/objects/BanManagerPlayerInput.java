package de.lacodev.rsystem.objects;

import org.bukkit.entity.Player;

public class BanManagerPlayerInput {

    private Player player;
    private String name;
    private final String Type;
    private int time;
    private String unit;

    public BanManagerPlayerInput(Player player, String name, int time, String unit, String type) {
        this.Type = type;
        this.player = player;
        this.name = name;
        this.time = time;
        this.unit = unit;
    }

    public String getType() {
        return Type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
