package com.tuvistavie.xserver.model

import com.tuvistavie.xserver.util.Properties.{ settings => Config }
import com.typesafe.config.ConfigValue
import java.util.HashMap
import akka.util.ByteString
import com.tuvistavie.xserver.util.ExtendedByteStringBuilder

class PixmapFormat(
  val depth: Int,
  val bitPerPixel: Int,
  val scanLinePad: Int
) {
  def toByteString: ByteString = {
    import ExtendedByteStringBuilder._
    val builder = ByteString.newBuilder
    builder.putByte(depth.toByte)
    builder.putByte(bitPerPixel.toByte)
    builder.putByte(scanLinePad.toByte)
    builder.fill(5)
    builder result
  }
}

object PixmapFormat {
  val formats: List[PixmapFormat] = loadFormats

  private[this] def loadFormats: List[PixmapFormat] = {
    val formatConfig = Config.getList("server.pixmap.formats").iterator()
    var f = List[PixmapFormat]()
    while(formatConfig.hasNext()) f :+= loadFormat(formatConfig.next())
    f
  }

  private[this] def loadFormat(config: ConfigValue): PixmapFormat = {
    val c = config.unwrapped().asInstanceOf[HashMap[String, Int]]
    new PixmapFormat(
      c.get("depth"),
      c.get("bit-per-pixel"),
      c.get("scanline-pad")
    )
  }

}
