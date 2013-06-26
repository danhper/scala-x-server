package com.tuvistavie.xserver.protocol

import com.tuvistavie.xserver.protocol.request._
import com.tuvistavie.xserver.protocol.reply._
import com.tuvistavie.xserver.backend.model.{ Extension, Property }

object ReplyBuilder {
  def buildReply(request: Request, sequenceNumber: Int): Reply = request match {
    case r: GetPropertyRequest => Property.generateGetPropertyReply(r, sequenceNumber)
    case r: QueryExtensionRequest => Extension.generateQueryExtensionReply(r, sequenceNumber)
  }
}
