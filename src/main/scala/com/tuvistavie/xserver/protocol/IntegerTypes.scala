package com.tuvistavie.xserver.protocol;

case class UInt8(val value: Int, val isBigEndian: Boolean = false) extends Value[Int](value, isBigEndian) {
  override def byteSize = 1

  override def toBytes = value.toString
  override def toJSON = value.toString
}

case class UInt16 extends Value[Int] {
  override def length = 2

}
