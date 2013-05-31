package com.tuvistavie.xserver.model

import com.tuvistavie.xserver.util.Properties.{ settings => Config }

class PixmapFormat (
)

object PixmapFormat {
  val formats: List[PixmapFormat] = loadFormats

  private[this] def loadFormats: List[PixmapFormat] = {
    val formatConfig = Config.getList("server.pixmap.formats")


  }

}
