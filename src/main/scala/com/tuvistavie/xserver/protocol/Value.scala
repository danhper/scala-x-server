package com.tuvistavie.xserver.protocol.types


abstract class Value {
  type T
  val value: T
  def byteSize: Int
}


