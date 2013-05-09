package com.tuvistavie.xserver.io

import java.io.OutputStream
import java.io.DataOutputStream
import com.tuvistavie.xserver.protocol.types._

abstract class BinaryOutputStream(
  val outputStream: OutputStream,
  val bigEndian: Boolean
) extends DataOutputStream(outputStream) {

  def writePad(n: IntValue) = fill(0, n.padding)

  def fill(byte: Card8, times: Int) = {
    1 to times foreach { _ => outputStream.write(byte) }
  }

  def writeInt8(n: Int8) = writeByte(n)
  def writeUInt8(n: UInt8) = writeByte(n)

}

class BinaryOutputStreamMSB (
  override val outputStream: DataOutputStream
) extends BinaryOutputStream(outputStream, true) {

}


class BinaryOutputStreamLSB(
  override val outputStream: DataOutputStream
) extends BinaryOutputStream(outputStream, false) {

}
