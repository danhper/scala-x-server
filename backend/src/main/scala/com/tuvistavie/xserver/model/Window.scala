package com.tuvistavie.xserver.backend.model

import com.tuvistavie.xserver.backend.util.{ Config, RuntimeConfig }

class Window (
  override val id: Int,
  val inputClass: InputClass,
  val width: Int,
  val height: Int
) extends Resource(id) {

}

object Window {
  val root = new Window(
    Config.getInt("server.screen.root-id"),
    InputClass.InputOutput,
    RuntimeConfig.rootWidth,
    RuntimeConfig.rootHeight
  )
}
