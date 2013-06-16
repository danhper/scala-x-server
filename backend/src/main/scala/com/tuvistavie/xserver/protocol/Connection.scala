package com.tuvistavie.xserver.backend.protocol

import akka.actor.IO
import akka.util.ByteString
import com.typesafe.scalalogging.slf4j.Logging
import com.tuvistavie.xserver.backend.util.{ ExtendedByteIterator, ExtendedByteStringBuilder, Conversions }
import Conversions._
import com.tuvistavie.xserver.backend.util.Config
import errors.ConnectionError
import misc.ProtocolException
import com.tuvistavie.xserver.backend.model.{ ServerInfo, PixmapFormat, Keyboard, Screen }


case class Connection (
  majorVersion: Int,
  minorVersion: Int,
  authProtocolName: Option[String],
  authProtocolData: Option[String]
)

object Connection extends Logging {
  import IO._
  import ExtendedByteIterator._
  import ExtendedByteStringBuilder._

  def readConnectionInfo()(implicit endian: java.nio.ByteOrder, socket: SocketHandle): Iteratee[Connection] = {
    for {
      info <- take(12)
      iterator = info.iterator
      _ = iterator.skip(2)
      majorVersion = iterator.getShort
      minorVersion = iterator.getShort
      n = iterator.getShort.toInt
      d = iterator.getShort.toInt
      _ = iterator.skip(2)
      extraInfo <- take(n.withPadding + d.withPadding)
      extraIterator = extraInfo.iterator
    } yield {
      val serverMajorVersion = Config.getInt("server.protocol.major-version")
      val serverMinorVersion = Config.getInt("server.protocol.minor-version")
      (majorVersion, minorVersion) match {
        case (serverMajorVersion, serverMinorVersion) => {
          logger.debug(s"initializing connection with X major version = ${majorVersion}")
          if(n > 0) {
            logger.debug(s"authentication required: length ${n})")
            val authProtocolName = extraIterator.getString(n)
            extraIterator.skipPadding(n.withPadding)
            val authProtocolData = extraIterator.getString(d)
            extraIterator.skipPadding(d.padding)
            Connection(majorVersion, minorVersion, Some(authProtocolName), Some(authProtocolData))
          } else {
            logger.debug("no authentication")
            Connection(majorVersion, minorVersion, None, None)
          }
        }
        case _ => throw new ProtocolException(ConnectionError("not supported protocol version"))
      }
    }
  }

  def getOkResponse(clientId: Int)(implicit endian: java.nio.ByteOrder): ByteString = {
    import com.tuvistavie.xserver.backend.util.Conversions._
    val replyLength = (
      8
    + 2 * PixmapFormat.formats.length
    + (
        Config.getString("server.info.vendor").length.withPadding
      + Screen.main.lengthInBytes
      ) / 4
    )
    logger.debug(s"calculated reply length: ${replyLength}")
    val builder = ByteString.newBuilder
    builder putByte(1) // success
    builder fill(1) // skip
    builder putShort(Config.getInt("server.protocol.major-version"))
    builder putShort(Config.getInt("server.protocol.minor-version"))
    builder putShort(replyLength)
    builder putInt(Config.getInt("server.info.release-version"))
    builder putInt(clientId << ServerInfo.clientOffset) // resource id base
    builder putInt(ServerInfo.idMask)
    builder putInt(Config.getInt("server.misc.motion-buffer-size"))
    builder putShort(Config.getString("server.info.vendor") length)
    builder putShort(Config.getInt("server.misc.maximum-request-length"))
    builder putByte(Config.getInt("server.display.number-of-screens"))
    logger.debug(s"${PixmapFormat.formats.length} pixmap formats")
    builder putByte(PixmapFormat.formats.length)
    builder putByte(Config.getInt("server.image.byte-order"))
    builder putByte(Config.getInt("server.bitmap.byte-order"))
    builder putByte(Config.getInt("server.bitmap.scanline-unit"))
    builder putByte(Config.getInt("server.bitmap.scanline-pad"))
    builder putByte(Keyboard.minCode)
    builder putByte(Keyboard.maxCode)
    builder fill(4)
    builder putBytes(Config.getString("server.info.vendor") getBytes)
    builder writePadding(Config.getString("server.info.vendor") length)
    val serverInfo = builder result
    val pixmapFormats = (ByteString.empty /: PixmapFormat.formats.map(_ toByteString)) (_++_)
    val screenInfo = Screen.main toByteString

    serverInfo ++ pixmapFormats ++ screenInfo
  }
}
