package com.tuvistavie.xserver.protocol

import akka.actor.IO
import akka.util.ByteString
import com.typesafe.scalalogging.slf4j.Logging
import com.tuvistavie.xserver.util.{ ExtendedByteIterator, ExtendedByteStringBuilder}
import com.tuvistavie.xserver.util.Properties.{ settings => Config }
import errors.ConnectionError
import misc.ProtocolException
import com.tuvistavie.xserver.model.{ ServerInfo, PixmapFormat, Keyboard }


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
      n = iterator.getShort
      d = iterator.getShort
    } yield {
      val serverMajorVersion = Config.getInt("server.protocol.major-version")
      val serverMinorVersion = Config.getInt("server.protocol.minor-version")
      (majorVersion, minorVersion) match {
        case (serverMajorVersion, serverMinorVersion) => {
          logger.debug(s"initializing connection with X major version = ${majorVersion}")
          if(n > 0) {
            logger.debug("authentication required")
            val authProtocolName = iterator.getString(n)
            iterator.skipPadding(n)
            val authProtocolData = iterator.getString(d)
            iterator.skipPadding(d)
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
    import com.tuvistavie.xserver.util.Conversion._
    val builder = ByteString.newBuilder
    builder.putByte(1) // success
    builder.fill(1) // skip
    builder.putShort(Config.getInt("server.protocol.major-version"))
    builder.putShort(Config.getInt("server.protocol.minor-version"))
    builder.putInt(Config.getInt("server.info.release-number"))
    builder.putInt(clientId << ServerInfo.clientOffset) // resource id base
    builder.putInt(ServerInfo.idMask)
    builder.putInt(Config.getInt("server.misc.motion-buffer-size"))
    builder.putShort(Config.getString("server.info.vendor").length)
    builder.putShort(Config.getInt("server.misc.maximum-request-length"))
    builder.putByte(Config.getInt("server.display.number-of-screens"))
    builder.putByte(PixmapFormat.formats.length)
    builder.putByte(Config.getInt("server.image.byte-order"))
    builder.putByte(Config.getInt("server.bitmap.byte-order"))
    builder.putByte(Config.getInt("server.bitmap.scanline-unit"))
    builder.putByte(Config.getInt("server.bitmap.scanline-pad"))
    builder.putByte(Keyboard.minCode)
    builder.putByte(Keyboard.maxCode)
    builder.fill(4)
    builder.putBytes(Config.getString("server.info.vendor").getBytes())
    builder.writePadding(Config.getString("server.info.vendor").length)
    val serverInfo = builder.result
    val pixmapFormats: ByteString = PixmapFormat.formats map { _.toByteString } reduce (_++_)

    serverInfo ++ pixmapFormats
  }
}
