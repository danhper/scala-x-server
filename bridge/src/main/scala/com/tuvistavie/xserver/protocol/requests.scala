package com.tuvistavie.xserver.protocol

import akka.actor.IO
import com.typesafe.scalalogging.slf4j.Logging

trait Request

object Request extends Logging {
  import IO._

  def getRequest(opCode: Int)(implicit endian: java.nio.ByteOrder, socket: SocketHandle): Iteratee[Request] = {
    for {
      header <- take(3)
      iterator = header.iterator
      data = iterator.getByte
      requestLength = iterator.getShort
    } yield {
      logger.debug(s"handling request with opcode ${opCode} and length ${requestLength}")
      new Request {}
    }
  }

}
