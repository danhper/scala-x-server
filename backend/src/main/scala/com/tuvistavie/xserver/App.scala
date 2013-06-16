package com.tuvistavie.xserver.backend

import io.Server
import util.{ Config, InitSettings }
import com.tuvistavie.xserver.backend.model.PixmapFormat
import com.tuvistavie.xserver.bridge.BridgeClient

object App {
  def main(args: Array[String]) = {
    val initSettings = InitSettings.parse(args) match {
      case Some(s) => s
      case None => sys.exit(1)
    }
    val displayNumber = initSettings.displayNumber
    BridgeClient.register()
    Server.startUp(displayNumber)
  }
}
