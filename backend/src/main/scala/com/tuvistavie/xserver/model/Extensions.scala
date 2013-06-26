package com.tuvistavie.xserver.backend.model

import com.tuvistavie.xserver.protocol.request.QueryExtensionRequest
import com.tuvistavie.xserver.protocol.reply.QueryExtensionReply

class Extension (
  val majorOpcode: Int,
  val firstEvent: Int,
  val firstError: Int
)

object Extension {
  private var availableExtensions: Map[String, Extension] = Map()

  def register(name: String, extension: Extension) = availableExtensions += (name -> extension)

  def getExtension(name: String) = availableExtensions(name)

  def isAvailable(name: String) = availableExtensions.contains(name)

  def generateQueryExtensionReply(request: QueryExtensionRequest, sequenceNumber: Int) = {
    val name = request.name
    if(isAvailable(name)) {
      val ext = getExtension(name)
      QueryExtensionReply(sequenceNumber, true, ext.majorOpcode, ext.firstEvent, ext.firstError)
    } else QueryExtensionReply(sequenceNumber, false, 0, 0, 0)
  }
}
