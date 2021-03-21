package de.lacodev.rsystem.objects;

public class BanReasons {


  public String name;

  public Integer id;

  public long length;


  public BanReasons(String name, Integer id, long length) {

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
