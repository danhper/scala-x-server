package com.tuvistavie.xserver.backend.protocol.types

abstract class Value extends KnownSize {
  type T
  val value: T
}

trait KnownSize {
  def byteSize: Int
}

trait SingleByte extends KnownSize {
  override def byteSize = 1
}

trait DoubleByte extends KnownSize {
  override def byteSize = 2
}

trait FourBytes extends KnownSize {
  override def byteSize = 4
}
