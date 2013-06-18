package com.tuvistavie.xserver.protocol

import com.tuvistavie.xserver.protocol.request._
import com.tuvistavie.xserver.backend.model.Extension

object ReplyBuilder {
  def buildReply(request: Request, sequenceNumber: Int): Reply = request match {
    case QueryExtension(s) => buildQueryExtensionReply(s, sequenceNumber)
  }

  private def buildQueryExtensionReply(name: String, sequenceNumber: Int) = {
    if(Extension.isAvailable(name)) {
      val ext = Extension.getExtension(name)
      reply.QueryExtension(sequenceNumber, true, ext.majorOpcode, ext.firstEvent, ext.firstError)
    } else reply.QueryExtension(sequenceNumber, false, 0, 0, 0)
  }
}
