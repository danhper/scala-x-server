package com.tuvistavie.xserver.protocol.error

import akka.util.ByteString

import com.tuvistavie.xserver.backend.util.Conversions._
import com.tuvistavie.xserver.backend.util.ExtendedByteStringBuilder._
import com.tuvistavie.xserver.backend.util.Config


case class ConnectionError (
  message: String
) (
  implicit val endian: java.nio.ByteOrder
) extends ErrorLike {
  def toByteString = {
    var frameBuilder = ByteString.newBuilder
    val n = message.length
    frameBuilder putByte 0 // error
    frameBuilder putByte n.toByte
    frameBuilder putShort Config.getInt("server.protocol.major-version")
    frameBuilder putShort Config.getInt("server.protocol.minor-version")
    frameBuilder putShort (n.withPadding / 4)
    frameBuilder putBytes message.getBytes
    frameBuilder putPadding n
    frameBuilder result
  }
}
