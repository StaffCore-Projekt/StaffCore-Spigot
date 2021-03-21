package de.lacodev.rsystem.objects;

public class MuteReasons {

  public String name;

  public Integer id;

  public long length;


  public MuteReasons(String name, Integer id, long length) {

    this.name = name;

    this.id = id;

    this.length = length;

  }


  public long getLength() {

    return this.length;

  }

  public String getName() {

    return this.name;

  }


  public Integer getID() {

    return this.id;

  }
}
