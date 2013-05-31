package com.tuvistavie.xserver

import io.Server
import util.Properties.{ settings => Config }
import com.tuvistavie.xserver.model.PixmapFormat

object App {
  def main(args: Array[String]) = {
    val displayNumber = Config.getInt("server.display.default")
    Server.startUp(displayNumber)
  }
}
