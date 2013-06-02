package com.tuvistavie.xserver.model

class Resource (
  val id: Int
) {

}

object Resource {
  private[this] var allocated: Map[Int, Resource] = Map.empty

  def canAllocate(id: Int) = !allocated.contains(id)

  def isAllocated(id: Int) = allocated.contains(id)

  def allocate(id: Int, resource: Resource): Boolean = {
    if(canAllocate(id)) {
      allocated += (id -> resource)
      true
    }
    else false
  }

  def unallocate(id: Int, resource: Resource): Boolean = {
    if(isAllocated(id)) {
      allocated -= id
      true
    } else false
  }
}
