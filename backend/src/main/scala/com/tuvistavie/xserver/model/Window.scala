package com.tuvistavie.xserver.backend.model

import com.tuvistavie.xserver.backend.util.Config

class Window (
  override val id: Int
) extends Resource(id) {

}

object Window {
  val root = Window(Config.getInt("server.screen.root-id"))
  def apply(id: Int) = {
    if(Resource.canAllocate(id)) new Window(id)
    else throw new IllegalArgumentException("id " + id + " already allocated")
  }
}
