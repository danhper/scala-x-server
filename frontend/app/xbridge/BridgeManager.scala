package com.tuvistavie.xserver.frontend.xbridge

object BridgeManager {
  private var bridges = Map[Int, Bridge]()

  def create(id: Int, username: String): Bridge = {
    val bridge = new Bridge()
    bridges += (id -> bridge)
    bridge.launch(id, username)
    bridge
  }
}
