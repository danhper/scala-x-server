package com.tuvistavie.xserver

import io.Server
import util.Properties.{ settings => Config }

object App {
  def main(args: Array[String]) = {
    val displayNumber = Config.getInt("server.display.default")
    Server.startUp(displayNumber)
  }
}
