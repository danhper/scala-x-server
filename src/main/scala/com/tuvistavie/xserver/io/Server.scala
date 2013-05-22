package com.tuvistavie.xserver.io

import akka.actor._
import akka.util.{ ByteString, ByteStringBuilder }
import java.net.InetSocketAddress

import com.tuvistavie.xserver.protocol.Constants

class Server(displayNumber: Int) extends Actor {
  import IO._

  val port = displayNumber + Constants.basePort

  override def preStart {
    IOManager(context.system) listen new InetSocketAddress(port)
  }

  def receive() = {
    case NewClient(server) => {
      server.accept()(context.system.actorOf(Props[ClientManager]))
    }
  }
}

