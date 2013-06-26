package com.tuvistavie.xserver.backend.model;

public enum InputClass {
  InputOutput,
  InputOnly,
  CopyFromParent;

  public static InputClass fromInt(int i) {
    switch(i) {
      case 1: return InputOutput;
      case 2: return InputOnly;
      default: return CopyFromParent;
    }
  }
}
