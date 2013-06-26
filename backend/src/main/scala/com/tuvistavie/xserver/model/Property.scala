package com.tuvistavie.xserver.backend.model

import com.tuvistavie.xserver.protocol.Atom
import com.tuvistavie.xserver.protocol.request.GetPropertyRequest
import com.tuvistavie.xserver.protocol.reply.GetPropertyReply

class Property (
  val id: Atom,
  val propType: Atom,
  val format: Int,
  val value: List[Int]
)

object Property {
  private var properties: Map[Atom, Property] = Map()

  def set(property: Property) = properties += (property.id -> property)
  def get(id: Atom) = properties.get(id)
  def remove(id: Atom) = properties -= id

  def generateGetPropertyReply(request: GetPropertyRequest, sequenceNumber: Int) = get(request.property) match {
    case Some(prop) => {
      if(prop.propType != request.propType && request.propType != Atom.NIL) {
        GetPropertyReply(sequenceNumber, prop.format, prop.propType, 0, List.empty)
      } else {
        if(request.delete) properties -= request.property
        GetPropertyReply(sequenceNumber, prop.format, prop.propType, prop.value.length, prop.value)
      }
    }
    case None => {
      GetPropertyReply(sequenceNumber, 0, Atom.NIL, 0, List.empty)
    }
  }
}
