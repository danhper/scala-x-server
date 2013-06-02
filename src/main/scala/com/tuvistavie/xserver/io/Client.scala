package com.tuvistavie.xserver.io

import akka.actor.{Actor, IO}

import com.typesafe.scalalogging.slf4j.Logging

import com.tuvistavie.xserver.protocol.errors.{ BaseError, ConnectionError }
import com.tuvistavie.xserver.util.IntWithPad._
import com.tuvistavie.xserver.protocol.Connection
import com.tuvistavie.xserver.protocol.misc.ProtocolException

case class ClientConnectionAdded(socket: IO.Handle, client: Client)
case class ClientConnectionClosed(id: Int)

class ClientManager(private val id: Int) extends Actor with Logging {
  import IO._

  val it = new IterateeRefAsync(Iteratee.unit)(context.dispatcher)
  private var client: Option[Client] = None

  def receive = {
    case Read(socket, bytes) => {
      logger.debug(s"received client first packet ${bytes}")
      val client = Client(id, socket.asSocket, bytes.head.toChar)
      context.parent ! ClientConnectionAdded(socket, client)
      it(Chunk(bytes))
      context become(handleMessages)
      it flatMap ( _ => client.handleConnection )
      it flatMap ( _ => client.handleMessages )
    }
  }

  def handleMessages: Actor.Receive = {
    case Read(socket, bytes) => it(Chunk(bytes))
    case Closed(socket, cause) => {
      logger.info("client socket with uuid {} has been closed: {}", socket.uuid, cause)
      context.parent ! ClientConnectionClosed(id)
      context.stop(self)
    }
  }
}

object Client extends Logging {
  def apply(id: Int, socket: IO.SocketHandle, endian: Char) = endian match {
    case 'l' => {
      logger.debug("initializing new client with little endian")
      new Client(id, socket) with LittleEndian
    }
    case 'B' => {
      logger.debug("initializing new client with big endian")
      new Client(id, socket) with BigEndian
    }
    case _ => {
      logger.error(s"invalid endian info received: ${endian}")
      throw new ProtocolException(ConnectionError("invalid endian") (java.nio.ByteOrder.LITTLE_ENDIAN))(socket)
    }
  }
}

abstract class Client(id: Int, handle: IO.SocketHandle) extends Logging {
  import IO._

  implicit val endian: java.nio.ByteOrder
  implicit val socket: SocketHandle = handle

  def handleConnection: Iteratee[Unit] = {
    for {
      connection <- Connection.readConnectionInfo()
    } yield {
      connection match {
        case Connection(_, _, None, None) => {
          socket write Connection.getOkResponse(id)
          logger.debug("sent ok response for connection")
        }
        case _ => {
          socket write ConnectionError("authentication not supported").toBytes
          logger.debug("refused connection needing auth")
        }
      }
    }
  }

  def handleMessages: Iteratee[Unit] = repeat {
    for {
      a <- take(1)
    } yield {
      println(a)
    }
  }
}
