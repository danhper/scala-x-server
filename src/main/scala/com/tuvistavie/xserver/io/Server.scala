package com.tuvistavie.xserver.io

import akka.actor.{ Actor, ActorRef, IO, IOManager, ActorLogging, Props, ActorSystem, Terminated }
import akka.util.{ ByteString, ByteStringBuilder }
import java.net.InetSocketAddress
import com.tuvistavie.xserver.util.Properties.{settings => Config}
import com.typesafe.scalalogging.slf4j.Logging

private class Server(displayNumber: Int) extends Actor with ActorLogging {
  import IO._

  val port = displayNumber + Config.getInt("server.base-port")

  override def preStart {
    IOManager(context.system) listen new InetSocketAddress(port)
    log.info("Server started on port {} ", port)
  }

  def receive() = {
    case NewClient(server) => {
      val child = context.actorOf(Props(new ClientManager(Server.currentId)))
      server.accept()(child)
      log.debug("new client connected")
    }
    case ClientConnectionAdded(socket, client) => {
      Server.addClient(client)
    }
    case ClientConnectionClosed(clientId) => {
      Server.removeClient(clientId)
    }
  }
}

object Server extends Logging {
  val system = ActorSystem("Server")

  private var _ref: Option[ActorRef] = None
  def ref = _ref.get

  private[this] var clientsById = Map[Int, Client]()
  private var _currentId = 0
  private def currentId = _currentId

  private def addClient(client: Client) {
    clientsById += (currentId -> client)
    logger.debug(s"added client with id: ${currentId}")
    _currentId += 1
  }

  private def removeClient(clientId: Int) {
    clientsById -= clientId
    logger.debug(s"removed client with id: ${clientId}")
  }

  def getClient(clientId: Int) {
    clientsById get(clientId)
  }

  def startUp(displayNumber: Int) = _ref match {
    case None => {
      _ref = Some(system.actorOf(Props(new Server(displayNumber))))
    }
    case Some(_) => throw new InstantiationError("Server is already running")
  }
}
