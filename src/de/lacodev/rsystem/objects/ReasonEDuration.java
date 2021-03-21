package de.lacodev.rsystem.objects;

import org.bukkit.entity.Player;

public class ReasonEDuration {

  private Player p;
  private int id;
  private long duration;
  private String unit;


  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Player getP() {
    return p;
  }

  public void setP(Player p) {
    this.p = p;
  }
}
