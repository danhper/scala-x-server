package com.tuvistavie.xserver.protocol.reply

import akka.util.ByteString
import com.tuvistavie.xserver.protocol.Atom
import com.tuvistavie.xserver.backend.util.{ ExtendedByteStringBuilder, Conversions }
import ExtendedByteStringBuilder._
import Conversions._
import com.tuvistavie.xserver.protocol.request._

trait Reply {
  val data: Int
  val sequenceNumber: Int
  val replyLength: Int

  def toByteString(implicit endian: java.nio.ByteOrder): ByteString = {
    val builder = ByteString.newBuilder
    builder putByte(1)
    builder putByte(data toByte)
    builder putShort(sequenceNumber)
    builder putInt(replyLength)
    builder.result ++ dataToByteString
  }

  protected def dataToByteString(implicit endian: java.nio.ByteOrder): ByteString
}

case class QueryExtensionReply (
  sequenceNumber: Int,
  present: Boolean,
  majorOpcode: Int,
  firstEvent: Int,
  firstError:Int
  ) extends Reply {
  val data = 0
  val replyLength = 0

  protected override def dataToByteString(implicit endian: java.nio.ByteOrder) = {
    val builder = ByteString.newBuilder
    builder putBoolean present
    builder putByte majorOpcode
    builder putByte firstEvent
    builder putByte firstError
    builder fill 20
    builder.result
  }
}

case class GetPropertyReply (
  sequenceNumber: Int,
  format: Int,
  propertyType: Atom,
  bytesAfter: Int,
  value: List[Int]
) extends Reply {
  val data = format
  val replyLength = value.length.withPadding / 4

  protected override def dataToByteString(implicit endian: java.nio.ByteOrder) = {
    val builder = ByteString.newBuilder
    builder putInt propertyType.id
    builder putInt bytesAfter
    if(format == 0) {
      builder putInt 0
      builder fill 12
    } else {
      builder putInt (value.length * 8 / format)
      builder fill 12
      builder putIntList (value, format / 8)
      builder putPadding value.length
    }
    builder result
  }
}
