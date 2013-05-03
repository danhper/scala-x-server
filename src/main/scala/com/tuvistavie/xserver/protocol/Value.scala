package com.tuvistavie.xserver.protocol.types


abstract class Value extends ByteSerializable {
  type T
  val value: T
  def byteSize: Int
}


