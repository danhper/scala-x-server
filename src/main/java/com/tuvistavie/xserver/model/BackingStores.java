package com.tuvistavie.xserver.model;

public enum BackingStores{
  Never      (0),
  WhenMapped (1),
  Always     (2);

  private int id;

  BackingStores(int id) {
    this.id = id;
  }

  public int id() {
    return this.id;
  }
}
