package com.tuvistavie.xserver.io

import java.io.InputStream
import java.io.DataInputStream

import com.tuvistavie.xserver.protocol.types._

import com.tuvistavie.util._


abstract class BinaryInputStream(val inputStream: InputStream, val isBigEndian: Boolean) extends DataInputStream(inputStream) {

  def readSkip[T <: KnownSize](n: Int, valSize: Int)(implicit readVal: () => T): T

  def readPad(n: IntValue) = skipBytes(n.padding)

  implicit def readBool() = Bool(readBoolean())
  implicit def readInt8(): Int8 = Int8(readByte())
  implicit def readUInt8(): UInt8 = UInt8(readByte())
  implicit def readInt16(): Int16
  implicit def readInt16(bigEndian: Boolean): Int16 = {
    if(bigEndian) Int16(readShort())
    else Int16(readShort()).swapBytes
  }
  implicit def readUInt16(): UInt16
  implicit def readUInt16(bigEndian: Boolean): UInt16 = {
    if(bigEndian) UInt16(readUnsignedShort())
    else UInt16(readUnsignedShort()).swapBytes

  }
  def readInt32(): Int32
  def readInt32(bigEndian: Boolean): Int32 = {
    if(bigEndian) Int32(readInt())
    else Int32(readInt()).swapBytes
  }
  def readUInt32(): UInt32
  def readUInt32(bigEndian: Boolean): UInt32 = {
    if(bigEndian) UInt32(readInt())
    else UInt32(readInt()).swapBytes
  }

  def readBool(n: Int): Bool = readSkip[Bool](n, 1)
  def readInt8(n: Int): Int8 = readSkip[Int8](n, 1)
  def readUInt8(n: Int): UInt8 = readSkip[UInt8](n, 1)
  def readInt16(n: Int): Int16 = readSkip[Int16](n, 2)
  def readUInt16(n: Int): UInt16 = readSkip[UInt16](n, 2)


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



  def readList[T](n: Int)(implicit readVal: () => T) = {
    def loopRead(n: Int, list: List[T]): List[T] = n match {
      case 0 => list reverse
      case _ => loopRead(n - 1, readVal() :: list)
    }
    loopRead(n, List())
  }

  implicit def readStr(): Str = {
    val n = this.readByte()
    readString8(n)
  }

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

  implicit def readPoint() = {
    val x = readInt16()
    val y = readInt16()
    new Point(x, y)
  }

  implicit def readSegment() = {
    val origin = readPoint()
    val end = readPoint()
    new Segment(origin, end)
  }

  def readSize(): Size = {
    val width = readCard16()
    val height = readCard16()
    new Size(width, height)
  }

  implicit def readRectangle() = {
    val origin = readPoint()
    val size = readSize()
    new Rectangle(origin, size)
  }

  implicit def readArc() = {
    val origin = readPoint()
    val size = readSize()
    val angle1 = readInt16()
    val angle2 = readInt16()
    new Arc(origin, size, angle1, angle2)
  }

  implicit def readTextItem8(): TextItem = {
    val n = readCard8()
    if(n == 255) {
      val font = readFont(true)
      new TextItemBytes(font)
    } else {
      val delta = readInt8()
      val string = readString8(n)
      new TextItemString(delta, string)
    }
  }

  def readTextItem16(): TextItem = {
    val n = readCard8()
    if(n == 255) {
      val font = readFont(true)
      new TextItemBytes(font)
    } else {
      val delta = readInt8()
      val string = readString16(n)
      new TextItemString(delta, string)
    }
  }

  def readCard8(): Card8 = readUInt8()
  def readCard8(n: Int) = readUInt8(n)
  def readCard16() = readUInt16()
  def readCard16(n: Int) = readUInt16(n)
  def readCard32() = readUInt32()
  def readCard32(bigEndian: Boolean) = readUInt32(bigEndian)
  def readBitmask() = readCard32()
  def readWindow() = readCard32()
  def readPixmap() = readCard32()
  def readCursor() = readCard32()
  def readFont() = readCard32()
  def readFont(bigEndian: Boolean) = readCard32(bigEndian)
  def readGContext() = readCard32()
  def readColormap() = readCard32()
  def readDrawable() = readCard32()
  def readFontable() = readCard32()
  def readVisualID() = readCard32()
  def readTimestamp() = readCard32()
  def readKeycode() = readCard8()
  def readButton() = readCard8()


  def readListOfStr(n: Int) = readList[Str](n)
  def readListOfInt8(n: Int) = readList[Int8](n)
  def readListOfCard8(n: Int) = readList[Card8](n)
  def readListOfPoints(n: Int) = readList[Point](n)
  def readListOfRectangles(n: Int) = readList[Rectangle](n)
  def readListOfSegments(n: Int) = readList[Segment](n)
  def readListOfArcs(n: Int) = readList[Arc](n)
  def readListOfTextItem8(n: Int) = readList[TextItem](n)
  def readListOfTextItem16(n: Int) = readList[TextItem](n)(readTextItem16)
}

class BinaryInputStreamLSB(override val inputStream: InputStream) extends BinaryInputStream(inputStream, false) {

  override def readSkip[T <: KnownSize](n: Int, valSize: Int)(implicit readVal: () => T): T = {
    val value = readVal()
    skipBytes(n - valSize)
    value
  }

  override def readInt16() = readInt16(false)
  override def readUInt16() = readUInt16(false)

  override def readInt32() = readInt32(false)
  override def readUInt32() = readUInt32(false)
}

class BinaryInputStreamMSB(override val inputStream: InputStream) extends BinaryInputStream(inputStream, true) {

  override def readSkip[T <: KnownSize](n: Int, valSize: Int)(implicit readVal: () => T): T = {
    skipBytes(n - valSize)
    readVal()
  }

  override def readInt16() = readInt16(true)
  override def readUInt16() = readUInt16(true)

  override def readInt32() = readInt32(true)
  override def readUInt32() = readUInt32(true)
}

