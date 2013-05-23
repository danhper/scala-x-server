package com.tuvistavie.xserver.io

import akka.actor.{ Actor, IO, IOManager, ActorLogging, Props, ActorSystem }
import akka.util.{ ByteString, ByteStringBuilder }
import java.net.InetSocketAddress

import com.tuvistavie.xserver.util.Properties.{settings => Config}

class Server(displayNumber: Int) extends Actor with ActorLogging {
  import IO._

  val port = displayNumber + Config.getInt("server.base-port")

  override def preStart {
    IOManager(context.system) listen new InetSocketAddress(port)
    log.info("Server started on port {} ", port)
  }

  def receive() = {
    case NewClient(server) => {
      server.accept()(context.system.actorOf(Props[ClientManager]))
      log.debug("new client connected")
    }
  }
}

object Server {
  val system = ActorSystem("Server")

  def startUp(displayNumber: Int) {
    system.actorOf(Props(new Server(displayNumber)))
  }
}
