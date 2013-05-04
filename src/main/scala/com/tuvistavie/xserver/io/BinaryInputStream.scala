package com.tuvistavie.xserver.io

import java.io.InputStream
import java.io.DataInputStream

import com.tuvistavie.xserver.protocol.types._

import com.tuvistavie.util._

abstract class BinaryInputStream(val inputStream: DataInputStream) extends DataInputStream(inputStream) {

  def skip(n: IntTimes) = {
    n times { readInt8() }
  }

  def readPad(n: Int) = {
    val p = (4 - (n % 4)) % 4
    skip(p)
  }

  def readInt8() = Int8(inputStream.readByte())
  def readUInt8() = UInt8(inputStream.readByte())
  def readInt8(n: Int): Int8
  def readUInt8(n: Int): UInt8

  def readInt16(): Int16
  def readUInt16(): UInt16
  def readInt16(n: Int): Int16
  def readUInt16(n: Int): UInt16

  def readUInt32(): UInt32
  def readInt32(): Int32

  def readBool() = Bool(inputStream.readBoolean())
  def readBool(n: Int): Bool

  def readBitGravity() = readCard8().asBitGravity
  def readBitGravity(n: Int) = readCard8(n).asBitGravity
  def readWindowGravity() = readCard8().asWindowGravity
  def readWindowGravity(n: Int) = readCard8(n).asWindowGravity

  def readAtom() = readCard32().asAtom
  def readEventMask() = readCard32().asEventMask
  def readDeviceEventMask() = readCard32().asDeviceEventMask
  def readPointerEventMask() = readCard32().asPointerEventMask

  def readCard8() = readUInt8()
  def readCard8(n: Int) = readUInt8(n)
  def readCard16() = readUInt16()
  def readCard16(n: Int) = readUInt16(n)
  def readCard32() = readUInt32()
  def readBitmask() = readCard32()
  def readWindow() = readCard32()
  def readPixmap() = readCard32()
  def readCursor() = readCard32()
  def readFont() = readCard32()
  def readGContext() = readCard32()
  def readColormap() = readCard32()
  def readDrawable() = readCard32()
  def readFontable() = readCard32()
  def readVisualID() = readCard32()
  def readTimestamp() = readCard32()
  def readKeycode() = readCard8()
  def readButton() = readCard8()
}

class BinaryInputStreamLSB(override val inputStream: DataInputStream) extends BinaryInputStream(inputStream) {
  override def readInt8(n: Int): Int8 = {
    val value = readInt8()
    skip(n - 1)
    value
  }

  override def readUInt8(n: Int): UInt8 = {
    val value = readUInt8()
    skip(n - 1)
    value
  }

  override def readInt16(n: Int): Int16 = {
    val value = readInt16()
    skip(n - 2)
    value
  }

  override def readUInt16(n: Int): UInt16 = {
    val value = readUInt16()
    skip(n - 2)
    value
  }


  override def readBool(n: Int): Bool = {
    val value = readBoolean()
    skip(n - 1)
    Bool(value)
  }


  override def readInt16() = Int16(inputStream.readShort()).swapBytes
  override def readUInt16() = UInt16(inputStream.readShort()).swapBytes

  override def readInt32() = Int32(inputStream.readInt()).swapBytes
  override def readUInt32() = UInt32(inputStream.readInt()).swapBytes
}

class BinaryInputStreamMSB(override val inputStream: DataInputStream) extends BinaryInputStream(inputStream) {
  override def readInt8(n: Int): Int8 = {
    skip(n - 1)
    readInt8()
  }

  override def readUInt8(n: Int): UInt8 = {
    skip(n - 1)
    readUInt8()
  }

  override def readInt16(n: Int): Int16 = {
    skip(n - 2)
    readInt16()
  }

  override def readUInt16(n: Int): UInt16 = {
    skip(n - 2)
    readUInt16()
  }

  override def readBool(n: Int): Bool = {
    skip(n - 1)
    readBool()
  }

  override def readInt16() = Int16(inputStream.readShort())
  override def readUInt16() = UInt16(inputStream.readShort())

  override def readInt32() = Int32(inputStream.readInt())
  override def readUInt32() = UInt32(inputStream.readInt())
}

