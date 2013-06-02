package com.tuvistavie.xserver.util

class IntWithPad(val i: Int) {
  def padding = (4 - (i % 4)) % 4
  def withPadding = i + padding
}

object Conversions {
  implicit def shortToByte(short: Short): Byte = short toByte
  implicit def intToByte(int: Int): Byte = int toByte
  implicit def intToIntWithPad(i: Int) = new IntWithPad(i)
}
