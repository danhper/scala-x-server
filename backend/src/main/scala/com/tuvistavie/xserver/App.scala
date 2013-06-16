package com.tuvistavie.xserver.backend

import io.Server
import util.{ Config, RuntimeConfig, InitSettings, loadRuntimeConfig }
import com.tuvistavie.xserver.backend.model.PixmapFormat
import com.tuvistavie.xserver.bridge.{ BridgeClient, DummyBridgeClient }

object App {
  def main(args: Array[String]) = {
    InitSettings.parse(args) match {
      case Some(s) => loadRuntimeConfig(s)
      case None => sys.exit(1)
    }
    val displayNumber = RuntimeConfig.displayNumber
    val bridge = if(RuntimeConfig.standAlone) DummyBridgeClient
                 else BridgeClient
    bridge.register()
    Server.run()
  }
}
