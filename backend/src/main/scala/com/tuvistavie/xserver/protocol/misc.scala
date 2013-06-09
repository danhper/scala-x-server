package com.tuvistavie.xserver.backend.protocol.misc

import akka.actor.IO

import com.tuvistavie.xserver.backend.protocol.errors.BaseError

class ProtocolException(error: BaseError)(implicit sender: IO.SocketHandle) extends RuntimeException {
  sender.write(error.toBytes)
}
