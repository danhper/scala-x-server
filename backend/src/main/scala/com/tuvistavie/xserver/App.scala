package com.tuvistavie.xserver.backend

import io.Server
import util.Properties.{ settings => Config }
import com.tuvistavie.xserver.backend.model.PixmapFormat

object App {
  def main(args: Array[String]) = {
    val displayNumber = Config.getInt("server.display.default")
    Server.startUp(displayNumber)
  }
}
