package com.tuvistavie.xserver.protocol

import java.io.DataInputStream
import java.io.DataOutputStream

import com.tuvistavie.xserver.io._

abstract class IntValue(val value: Int) extends Value {
  type T = Int
}

object Int8 {
  implicit def intToInt8(i: Int): Int8 = Int8(i)
  implicit def int8ToInt(i: Int8): Int = i.value
}

case class Int8(override val value: Int) extends IntValue(value) {
  override def byteSize = 1

  def write(stream: BinaryOutputStream): Unit = {
    stream.writeByte(value)
  }

  def read(data: BinaryInputStream): Unit = {
  }
}

object Int16 {
  implicit def intToInt16(i: Int): Int16 = Int16(i)
  implicit def int16ToInt(i: Int16): Int = i.value
}

case class Int16(override val value: Int) extends IntValue(value) {
  override def byteSize = 2

  def swapBytes: Int16 = {
    ((value & 0xFF) << 8) | ((value >> 8) & 0xFF)
  }

  def read(data: BinaryInputStream): Unit = {
  }

  def write(stream: BinaryOutputStream): Unit = {
    val toWrite = if(stream.bigEndian) value else swapBytes.value
    stream.writeShort(toWrite)
  }
}

object Int32 {
  implicit def intToInt32(i: Int): Int32 = Int32(i)
  implicit def int32ToInt(i: Int32): Int = i.value
}

case class Int32(override val value: Int) extends IntValue(value) {
  override def byteSize = 4

  def swapBytes = {
    val newVal = ((value & 0XFF) << 32)  |
                 ((value & 0xFF00) << 8) |
                 ((value >> 8) & 0xFF00) |
                 ((value >> 24) & 0xFF)

    Int32(newVal)
  }

  def write(stream: BinaryOutputStream): Unit = {
    val toWrite = if(stream.bigEndian) value else swapBytes.value
    stream.writeInt(toWrite)
  }

  def read(data: BinaryInputStream): Unit = {
  }


}
