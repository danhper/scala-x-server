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

  def readInt16(): Int16
  def readInt32(): Int32
  def readUInt16(): UInt16
  def readUInt32(): UInt32
}

class BinaryInputStreamLSB(override val inputStream: DataInputStream) extends BinaryInputStream(inputStream) {
  def readInt16() = Int16(inputStream.readShort()).swapBytes
  def readUInt16() = UInt16(inputStream.readShort()).swapBytes

  def readInt32() = Int32(inputStream.readInt()).swapBytes
  def readUInt32() = UInt32(inputStream.readInt()).swapBytes
}

class BinaryInputStreamMSB(override val inputStream: DataInputStream) extends BinaryInputStream(inputStream) {
  def readInt16() = Int16(inputStream.readShort())
  def readUInt16() = UInt16(inputStream.readShort())

  def readInt32() = Int32(inputStream.readInt())
  def readUInt32() = UInt32(inputStream.readInt())
}
