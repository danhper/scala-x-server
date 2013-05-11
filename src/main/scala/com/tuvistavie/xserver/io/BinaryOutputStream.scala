package com.tuvistavie.xserver.io

import java.io.OutputStream
import java.io.DataOutputStream
import com.tuvistavie.xserver.protocol.types._

abstract class BinaryOutputStream(
  val outputStream: OutputStream,
  val bigEndian: Boolean
) extends DataOutputStream(outputStream) {

  def writePad(n: IntValue) = fill(0, n.padding)

  def writeInt16(u: Int16): Unit
  def writeUInt16(u: UInt16): Unit
  def writeInt32(u: Int32): Unit
  def writeUInt32(u: Card32): Unit

  def fill(byte: Card8, times: Int) = {
    1 to times foreach { _ => outputStream.write(byte) }
  }

  def writeInt8(n: Int8) = writeByte(n)
  def writeUInt8(n: UInt8) = writeByte(n)

  def writeCard8 = writeUInt8 _
  def writeCard16 = writeUInt16 _
  def writeCard32 = writeUInt32 _
  def writeBitmask = writeCard32 _
  def writeWindow = writeCard32 _
  def writePixmap = writeCard32 _
  def writeCursor = writeCard32 _
  def writeFont = writeCard32 _
  def writeGContext = writeCard32 _
  def writeColormap = writeCard32 _
  def writeDrawable = writeCard32 _
  def writeFontable = writeCard32 _
  def writeVisualID = writeCard32 _
  def writeTimestamp = writeCard32 _
  def writeKeysym = writeCard32 _
  def writeKeycode = writeCard8 _
  def writeButton = writeCard8 _
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
  override def writeInt16(u: Int16) = writeShort(u)
  override def writeUInt16(u: Card16) = writeShort(u)
  override def writeInt32(u: Int32) = writeInt(u)
  override def writeUInt32(u: Card32) = writeInt(u)
}

class BinaryOutputStreamLSB(
  override val outputStream: DataOutputStream
) extends BinaryOutputStream(outputStream, false) {
  override def writeInt16(u: Int16) = writeShort(u.swapBytes)
  override def writeUInt16(u: Card16) = writeShort(u.swapBytes)
  override def writeInt32(u: Int32) = writeInt(u.swapBytes)
  override def writeUInt32(u: Card32) = writeInt(u.swapBytes)
}
