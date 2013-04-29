package com.tuvistavie.xserver.protocol;

abstract class Value[T](val value: T, val isBigEndian: Boolean = false) {

  def byteSize: Int

  def swapBytes: Value[T] = this
  def toBytes: String = {
    if(isBigEndian) value.toString
    else swapBytes.value.toString
  }
  def toJSON: String = value.toString
}

trait TypeGenerator[T] {
  def fromBytes(bytes: Array[Byte]): Type[T]
  def fromJSON(json: String)
}
