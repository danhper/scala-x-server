package com.tuvistavie.xserver.io

import akka.actor.{ Actor, ActorRef, IO, IOManager, ActorLogging, Props, ActorSystem, Terminated }
import akka.util.{ ByteString, ByteStringBuilder }
import java.net.InetSocketAddress
import com.tuvistavie.xserver.util.Properties.{settings => Config}

private class Server(displayNumber: Int) extends Actor with ActorLogging {
  import IO._

  val port = displayNumber + Config.getInt("server.base-port")

  override def preStart {
    IOManager(context.system) listen new InetSocketAddress(port)
    log.info("Server started on port {} ", port)
  }

  def receive() = {
    case NewClient(server) => {
      val child = context.actorOf(Props[ClientManager])
      server.accept()(child)
      log.debug("new client connected")
    }
    case ClientConnectionAdded(socket, client) => {
      Server.clientsBySocket += (socket -> client)
      log.debug("client connection added")
    }
    case ClientConnectionClosed(socket) => {
      Server.clientsBySocket -= socket
      log.debug("client connection closed")
    }
  }
}

object Server {
  val system = ActorSystem("Server")
  private var _ref: Option[ActorRef] = None
  def ref = _ref.get

  private var clientsBySocket = Map[IO.Handle, Client]()
  private var clientsById = Map[IO.Handle, Client]()

  private var currentId = 0

  def startUp(displayNumber: Int) = _ref match {
    case None => {
      _ref = Some(system.actorOf(Props(new Server(displayNumber))))
    }
    case Some(_) => throw new InstantiationError("Server is already running")
  }
}
