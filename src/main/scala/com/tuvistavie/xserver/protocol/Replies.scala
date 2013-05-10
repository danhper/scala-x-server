package com.tuvistavie.xserver.protocol.reply

import com.tuvistavie.xserver.io._
import com.tuvistavie.xserver.protocol.types._

abstract class Reply(
  val data: Option[Card8],
  val sequenceNumber: Card16,
  val length: Card32
) {
  def write(stream: BinaryOutputStream) = {
    stream.writeByte(1)
    data match {
      case Some(n) => stream.writeByte(n)
      case _ => stream.writeByte(0)
    }
    stream.writeUInt16(sequenceNumber)
    stream.writeUInt32(length)
  }


}

case class GetWindowAttributes (

)
