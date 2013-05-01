package com.tuvistavie.xserver.protocol

class ReparentWindow(val window: Int32, val unused: Int8, val parent: Int32, val x: Int16, val y: Int16) {
  val opcode: Int8 = 8
}
