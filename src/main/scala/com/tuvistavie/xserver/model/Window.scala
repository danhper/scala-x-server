package com.tuvistavie.xserver.model

class Window (
  override val id: Int
) extends Resource(id) {

}

object Window {
  def apply(id: Int) = {
    if(Resource.canAllocate(id)) new Window(id)
    else throw new IllegalArgumentException("id " + id + " already allocated")
  }
}
