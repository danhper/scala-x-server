package com.tuvistavie.xserver.backend.model

import com.tuvistavie.xserver.backend.protocol.Atom

class Property (
  val id: Atom,
  val propType: Atom,
  val value: List[Int],
  val valueByteNum: Int
)

object Property {
  private var properties: Map[Atom, Property] = Map()

  def set(property: Property) = properties += (property.id -> property)
  def get(id: Atom) = properties.get(id)
  def remove(id: Atom) = properties -= id
}
