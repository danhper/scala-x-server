package com.tuvistavie.xserver.io

import akka.actor.IO

class Client(socket: IO.SocketHandle) {
  import IO._

  def handleMessages: Iteratee[Unit] = repeat {
    for {
      a <- take(1)
    } yield {

    }
  }
}
