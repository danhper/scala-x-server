package com.tuvistavie.xserver.io

import java.io.InputStream
import java.io.DataInputStream

import com.tuvistavie.xserver.protocol.types._

import com.tuvistavie.util._


abstract class BinaryInputStream(val inputStream: InputStream) extends DataInputStream(inputStream) {

  def skip(n: IntTimes) = {
    n times { readInt8() }
  }

  def readStr(): Str = {
    val n = this.readByte()
    readString8(n)
  }

  def readList[T](n: Int)(implicit readVal: () => T) = {
    def loopRead(n: Int, list: List[T]): List[T] = n match {
      case 0 => list reverse
      case _ => loopRead(n - 1, readVal() :: list)
    }
    loopRead(n, List())
  }

  def readListOfStr(n: Int) = readList[Str](n)(readStr)
  def readListOfCard8(n: Int) = readList[Card8](n)(readCard8)
  def readListOfPoints(n: Int) = readList[Point](n)(readPoint)
  def readListOfRectangles(n: Int) = readList[Rectangle](n)(readRectangle)
  def readListOfSegments(n: Int) = readList[Segment](n)(readSegment)


  def readString8(n: Int) = {
    var bytes = new Array[Byte](n)
    read(bytes, 0, n)
    Str(bytes)
  }

  def readString16(n: Int) = {
    def loopRead(n: Int, str: String): String = n match {
      case 0 => str
      case _ => loopRead(n - 1, str + this.readUnsignedShort())
    }
    Str(loopRead(n, ""))
  }

  def readPoint() = {
    val x = readInt16()
    val y = readInt16()
    new Point(x, y)
  }

  def readSegment() = {
    val origin = readPoint()
    val end = readPoint()
    new Segment(origin, end)
  }

  def readSize() = {
    val width = readCard16()
    val height = readCard16()
    new Size(width, height)
  }

  def readRectangle() = {
    val origin = readPoint()
    val size = readSize()
    new Rectangle(origin, size)
  }

  def readSkip[T <: KnownSize](n: Int)(implicit valSize: Int, readVal: () => T): T

  def readPad(n: IntValue) = skip(n.padding)

  def readInt8() = Int8(readByte())
  def readInt8(n: Int): Int8 = readSkip[Int8](n)(1, readInt8)
  def readUInt8() = UInt8(readByte())
  def readUInt8(n: Int): UInt8 = readSkip[UInt8](n)(1, readUInt8)

  def readBool() = Bool(readBoolean())
  def readBool(n: Int): Bool = readSkip[Bool](n)(1, readBool)


  def readInt16(): Int16
  def readInt16(n: Int): Int16 = readSkip[Int16](n)(2, readInt16)
  def readUInt16(): UInt16
  def readUInt16(n: Int): UInt16 = readSkip[UInt16](n)(2, readUInt16)

  def readUInt32(): UInt32
  def readInt32(): Int32


  def readBitGravity() = readCard8().asBitGravity
  def readBitGravity(n: Int) = readCard8(n).asBitGravity
  def readWindowGravity() = readCard8().asWindowGravity
  def readWindowGravity(n: Int) = readCard8(n).asWindowGravity

  def readSetOfKeyMask() = readCard16().asSetOfKeyMask
  def readSetOfKeyButMask() = readCard16().asSetOfKeyButMask

  def readAtom() = readCard32().asAtom
  def readSetOfEvent() = readCard32().asSetOfEvent
  def readSetOfPointerEvent() = readCard32().asSetOfPointerEvent
  def readSetOfDeviceEvent() = readCard32().asSetOfDeviceEvent

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

class BinaryInputStreamLSB(override val inputStream: InputStream) extends BinaryInputStream(inputStream) {

  override def readSkip[T <: KnownSize](n: Int)(implicit valSize: Int, readVal: () => T): T = {
    val value = readVal()
    skip(n - valSize)
    value
  }

  override def readInt16() = Int16(readShort()).swapBytes
  override def readUInt16() = UInt16(readShort()).swapBytes

  override def readInt32() = Int32(this.readInt()).swapBytes
  override def readUInt32() = UInt32(this.readInt()).swapBytes
}

class BinaryInputStreamMSB(override val inputStream: InputStream) extends BinaryInputStream(inputStream) {

  override def readSkip[T <: KnownSize](n: Int)(implicit valSize: Int, readVal: () => T): T = {
    skip(n - valSize)
    readVal()
  }

  override def readInt16() = Int16(this.readShort())
  override def readUInt16() = UInt16(this.readShort())

  override def readInt32() = Int32(this.readInt())
  override def readUInt32() = UInt32(this.readInt())
}

