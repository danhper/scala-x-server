package com.tuvistavie.xserver.backend.model;

public enum ColorClass {
  StaticGray  (0),
  GrayScale   (1),
  StaticColor (2),
  PseudoColor (3),
  TrueColor   (4),
  DirectColor (5);

  private int id;

  ColorClass(int id) {
    this.id = id;
  }

  public int id() {
    return this.id;
  }
}
