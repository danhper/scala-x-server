package com.tuvistavie.xserver.io

import java.io.InputStream
import java.io.DataInputStream

import com.tuvistavie.xserver.protocol._;

abstract class BinaryInputStream(val inputStream: DataInputStream) extends DataInputStream(inputStream) {

  def readInt8() = {
    val byte = inputStream.readByte()
    Int8(byte)
  }
  def readInt16: Int16
  def readInt32: Int32
}
