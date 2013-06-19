package com.tuvistavie.xserver.protocol

import com.tuvistavie.xserver.protocol.request._
import com.tuvistavie.xserver.protocol.reply._
import com.tuvistavie.xserver.backend.model.Extension

object ReplyBuilder {
  def buildReply(request: Request, sequenceNumber: Int): Reply = request match {
    case QueryExtensionRequest(s) => buildQueryExtensionReply(s, sequenceNumber)
  }

  private def buildQueryExtensionReply(name: String, sequenceNumber: Int) = {
    if(Extension.isAvailable(name)) {
      val ext = Extension.getExtension(name)
      QueryExtensionReply(sequenceNumber, true, ext.majorOpcode, ext.firstEvent, ext.firstError)
    } else QueryExtensionReply(sequenceNumber, false, 0, 0, 0)
  }
}
