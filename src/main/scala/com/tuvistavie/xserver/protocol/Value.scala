package com.tuvistavie.xserver.protocol


abstract class Value extends ByteSerializable {
  type T
  val value: T
  def byteSize: Int
}


