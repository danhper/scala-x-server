package com.tuvistavie.xserver.protocol.reply

import akka.util.ByteString

import com.tuvistavie.xserver.backend.util.{ ExtendedByteStringBuilder, Conversions }
import ExtendedByteStringBuilder._
import Conversions._

abstract class Reply (
  val data: Int,
  val sequenceNumber: Int,
  val replyLength: Int
) {
  def toBytes(implicit endian: java.nio.ByteOrder): ByteString = {
    val builder = ByteString.newBuilder
    builder putByte(1)
    builder putByte(data toByte)
    builder putShort(sequenceNumber)
    builder putInt(replyLength)
    builder result
  }
}


case class QueryExtensionReply (
  override val sequenceNumber: Int,
  present: Boolean,
  majorOpcode: Int,
  firstEvent: Int,
  firstError:Int
  ) extends Reply(0, sequenceNumber, 0) {
  override def toBytes(implicit endian: java.nio.ByteOrder): ByteString = {
    val baseBuilder = super.toBytes
    val builder = ByteString.newBuilder
    builder putBoolean present
    builder putByte majorOpcode
    builder putByte firstEvent
    builder putByte firstError
    builder fill 20
    baseBuilder ++ builder.result
  }
}
