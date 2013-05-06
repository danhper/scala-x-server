package com.tuvistavie.xserver.protocol.types

import java.io.DataInputStream
import java.io.DataOutputStream

import com.tuvistavie.xserver.io._
import atoms.Atom

abstract class IntValue(val value: Int) extends Value {
  type T = Int

  def padding = (4 - (value % 4)) % 4

  def toBool = Bool(value != 0)

}

abstract class Int8Value(override val value: Int) extends IntValue(value) with SingleByte {

  def write(stream: BinaryOutputStream): Unit = {
    stream.writeByte(value)
  }

  def read(data: BinaryInputStream): Unit = {
  }
}

object Int8 extends SingleByte {
  def apply(value: Int) = new Int8(value)
}
class Int8(override val value: Int) extends Int8Value(value)

object UInt8 extends SingleByte {
  def apply(value: Int) = new UInt8(value & 0xff)
}

class UInt8(override val value: Int) extends Int8Value(value) {
  def asBitGravity = BitGravity.fromValue(value)
  def asWindowGravity = WindowGravity.fromValue(value)
  def toInt8 = Int8(value.toByte)
}

object Int8Value extends SingleByte {
  implicit def intToInt8(i: Int): Int8 = Int8(i)
  implicit def intToUInt8(i: Int): UInt8 = UInt8(i)
  implicit def int8ValueToInt(i: Int8Value): Int = i.value
}

abstract class Int16Value(override val value: Int) extends IntValue(value) with DoubleByte {
  def swappedValue: Int = {
    ((value & 0xff) << 8) | ((value >> 8) & 0xff)
  }

  def swapBytes: Int16Value

  def read(data: BinaryInputStream): Unit = {
  }

  def write(stream: BinaryOutputStream): Unit = {
    val toWrite = if(stream.bigEndian) value else swapBytes.value
    stream.writeShort(toWrite)
  }

  def toUInt8 = UInt8(value)
}

object Int16Value extends DoubleByte {
  implicit def intToInt16(i: Int): Int16 = Int16(i)
  implicit def intToUInt16(i: Int): UInt16 = UInt16(i)
  implicit def int16ValueToInt(i: Int16Value): Int = i.value
}


class UInt16(override val value: Int) extends Int16Value(value) {
  def swapBytes = UInt16(swappedValue)
  def asSetOfKeyMask = SetOfKeyMask.fromMask(value)
  def asSetOfKeyButMask = SetOfKeyButMask.fromMask(value)
}

object UInt16 extends DoubleByte {
  def apply(value: Int) = new UInt16(value & 0xffff)
}

class Int16(override val value: Int) extends Int16Value(value) {
  def swapBytes = Int16(swappedValue)
}

object Int16 extends DoubleByte {
  def apply(value: Int) = new Int16(value)
}

abstract class Int32Value(override val value: Int) extends IntValue(value) with FourBytes {
  def swapBytes: Int32Value

  def swappedValue = {
    ((value & 0XFF) << 32)  |
    ((value & 0xFF00) << 8) |
    ((value >> 8) & 0xFF00) |
    ((value >> 24) & 0xFF)
  }

  def write(stream: BinaryOutputStream): Unit = {
    val toWrite = if(stream.bigEndian) value else swapBytes.value
    stream.writeInt(toWrite)
  }

  def read(data: BinaryInputStream): Unit = {
  }

  def toUInt16 = UInt16(value & 0xffff)

  def toUInt8 = UInt8(value & 0xff)
}

object Int32Value extends FourBytes {
  implicit def intToInt32(i: Int): Int32 = Int32(i)
  implicit def intToUInt32(i: Int): UInt32 = UInt32(i)
  implicit def int32ValueToInt(i: Int32Value): Int = i.value
}

class UInt32(override val value: Int) extends Int32Value(value) {
  def swapBytes = UInt32(swappedValue)
  def asAtom = Atom.fromValue(value)
  def asSetOfEvent = SetOfEvent.fromMask(value)
  def asSetOfPointerEvent = SetOfPointerEvent.fromMask(value)
  def asSetOfDeviceEvent = SetOfDeviceEvent.fromMask(value)
}

object UInt32 extends FourBytes {
  def apply(value: Int) = new UInt32(value & 0xffffffff)
}

class Int32(override val value: Int) extends Int32Value(value) {
  def swapBytes = Int32(swappedValue)
}

object Int32 extends FourBytes {
  def apply(value: Int) = new Int32(value)
}
