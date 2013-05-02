package com.tuvistavie.xserver.io

import java.io.InputStream
import java.io.DataInputStream

import com.tuvistavie.xserver.protocol._;

import com.tuvistavie.util._

abstract class BinaryInputStream(val inputStream: DataInputStream) extends DataInputStream(inputStream) {

  def skip(n: IntTimes) = {
    n times { readInt8() }
  }

  def readPad(n: Int) = {
    val p = (4 - (n % 4)) % 4
    skip(p)
  }

  def readInt8() = {
    val byte = inputStream.readByte()
    Int8(byte)
  }
  def readInt16(): Int16
  def readInt32(): Int32
}
