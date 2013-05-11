package com.tuvistavie.xserver.io

import java.io.OutputStream
import java.io.DataOutputStream
import com.tuvistavie.xserver.protocol.types._

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
  def writeInt32Value(v: Int32Value): Unit

  def writeAtom(a: Atom) = writeInt32Value(a.toUInt32)

  def writeStr(s: Str) = {
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

  def writeList[T](list: List[T])(implicit writeFun: T => Unit) = {
    list.foreach { writeFun(_) }
  }

  def writeCard8List(list: List[Card8]) = writeList[Card8](list)

  def writeInt8 = writeInt8Value _
  def writeUInt8 = writeInt8Value _
  def writeInt16 = writeInt16Value _
  def writeUInt16 = writeInt16Value _
  def writeInt32 = writeInt32Value _
  def writeUInt32 = writeInt32Value _

  def writeCard8 = writeUInt8
  def writeCard16 = writeUInt16
  def writeCard32 = writeUInt32
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
