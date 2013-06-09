package com.tuvistavie.xserver.backend.io

import scala.language.implicitConversions

import java.io.OutputStream
import java.io.DataOutputStream

import com.tuvistavie.xserver.backend.protocol.types._

abstract class BinaryOutputStream(
  val outputStream: OutputStream,
  val bigEndian: Boolean
) extends DataOutputStream(outputStream) {

  def writePad(n: IntValue) = fill(n.padding)

  def fill(n: Int, value: Int = 0) = {
    1 to n foreach { _ => outputStream.write(value) }
  }

  def writeBool(b: Bool) = writeByte(b.toByte)
  implicit def writeInt8Value(v: Int8Value): Unit = writeByte(v)
  def writeInt16Value(v: Int16Value): Unit
  implicit def writeInt32Value(v: Int32Value): Unit

  implicit def writeAtom(a: Atom) = writeInt32Value(a.toUInt32)

  implicit def writeStr(s: Str): Unit = {
    writeByte(s.byteSize)
    writeString8(s)
  }

  def writeString8(s: Str) = write(s.toByteArray, 0, s.byteSize)

  def writeSet(s: SetOf[_]) = {
    if(s.byteSize == 2) writeCard16(s.toCard16)
    else writeCard32(s.toCard32)
  }

  def writeValue(v: Value): Unit = v match {
    case b: Bool => writeBool(b)
    case i: Int8Value => writeInt8Value(i)
    case i: Int16Value => writeInt16Value(i)
    case i: Int32Value => writeInt32Value(i)
    case s: Str => writeStr(s)
    case s: SetOf[_] => writeSet(s)
  }

  def writePoint(p: Point) = {
    writeInt16(p.x)
    writeInt16(p.y)
  }

  def writeSize(s: Size) = {
    writeCard16(s.width)
    writeCard16(s.height)
  }

  implicit def writeHost(h: Host) = {
    writeCard8(h.family)
    fill(1)
    writeCard16(h.address.byteSize)
    writeString8(h.address)
    writePad(h.address.byteSize)
  }

  implicit def writeTimeCoord(timeCoord: TimeCoord) = {
    writeTimestamp(timeCoord.time)
    writePoint(timeCoord.point)
  }

  implicit def writeFontProp(fontProp: FontProp) = {
    writeAtom(fontProp.name)
    writeCard32(fontProp.value)
  }

  implicit def writeCharInfo(charInfo: CharInfo) = {
    writeInt16(charInfo.leftSideBearing)
    writeInt16(charInfo.rightSideBearing)
    writeInt16(charInfo.characterWidth)
    writeInt16(charInfo.ascent)
    writeInt16(charInfo.descent)
    writeCard16(charInfo.attributes)
  }

  implicit def writeRGB(rgb: RGB) = {
    writeCard16(rgb.red)
    writeCard16(rgb.green)
    writeCard16(rgb.blue)
    fill(2)
  }

  def writeList[T](list: List[T])(implicit writeFun: T => Unit) = {
    list.foreach { writeFun(_) }
  }

  def writeInt8List(list: List[Int8]) = writeList[Int8](list)
  def writeCard8List(list: List[Card8]) = writeList[Card8](list)
  def writeCard32List(list: List[Card32]) = writeList[Card32](list)
  def writeStrList(list: List[Str]) = writeList[Str](list)
  def writeAtomList(list: List[Atom]) = writeList[Atom](list)
  def writeTimeCoordList(list: List[TimeCoord]) = writeList[TimeCoord](list)
  def writeFontPropList(list: List[FontProp]) = writeList[FontProp](list)
  def writeCharInfoList(list: List[CharInfo]) = writeList[CharInfo](list)
  def writeRGBList(list: List[RGB]) = writeList[RGB](list)
  def writeHostList(list: List[Host]) = writeList[Host](list)

  def writeInt8(i: Int8) = writeInt8Value(i)
  def writeUInt8(u: UInt8) = writeInt8Value(u)
  def writeInt16(i: Int16) = writeInt16Value(i)
  def writeUInt16(u: UInt16) = writeInt16Value(u)
  def writeInt32(i: Int32) = writeInt32Value(i)
  def writeUInt32(u: UInt32) = writeInt32Value(u)

  def writeCard8 = writeUInt8 _
  def writeCard16 = writeUInt16 _
  def writeCard32 = writeUInt32 _

  def writeBitmask = writeCard32
  def writeWindow = writeCard32
  def writePixmap = writeCard32
  def writeCursor = writeCard32
  def writeFont = writeCard32
  def writeGContext = writeCard32
  def writeColormap = writeCard32
  def writeDrawable = writeCard32
  def writeFontable = writeCard32
  def writeVisualID = writeCard32
  def writeTimestamp = writeCard32
  def writeKeysym = writeCard32
  def writeKeycode = writeCard8
  def writeButton = writeCard8
}

object BinaryOutputStream {
  def apply(stream: DataOutputStream, bigEndian: Boolean) = {
    if(bigEndian) new BinaryOutputStreamMSB(stream)
    else new BinaryOutputStreamLSB(stream)
  }
}

class BinaryOutputStreamMSB (
  override val outputStream: DataOutputStream
) extends BinaryOutputStream(outputStream, true) {
  override def writeInt16Value(v: Int16Value) = writeShort(v)
  override def writeInt32Value(v: Int32Value) = writeInt(v)
}

class BinaryOutputStreamLSB(
  override val outputStream: DataOutputStream
) extends BinaryOutputStream(outputStream, false) {
  override def writeInt16Value(v: Int16Value) = writeShort(v.swapBytes)
  override def writeInt32Value(v: Int32Value) = writeInt(v.swapBytes)
}
