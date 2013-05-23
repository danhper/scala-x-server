package com.tuvistavie.xserver.io

import akka.actor.{Actor, IO}

import com.typesafe.scalalogging.slf4j.Logging

import com.tuvistavie.xserver.protocol.errors.{ BaseError, ConnectionError }
import com.tuvistavie.xserver.util.IntWithPad._
import com.tuvistavie.xserver.util.Properties.{ settings => Config }
import com.tuvistavie.xserver.protocol.Connection
import com.tuvistavie.xserver.protocol.misc.ProtocolException

class ClientManager extends Actor with Logging {
  import IO._

  val it = new IterateeRefAsync(Iteratee.unit)(context.dispatcher)

  def receive = {
    case Read(handle, bytes) => {
      logger.debug(s"received client first packet ${bytes}")
      val client = Client(handle.asSocket, bytes.head.toChar)
      it(Chunk(bytes))
      context become(handleMessages)
      it flatMap ( _ => client.handleConnection )
      it flatMap ( _ => client.handleMessages )
    }
  }

  def handleMessages: Actor.Receive = {
    case Read(socket, bytes) => it(Chunk(bytes))
    case Closed(socket: SocketHandle, cause) => {
      logger.info("client socket has been closed: {}", cause)
    }
  }
}

object Client extends Logging {
  def apply(socket: IO.SocketHandle, endian: Char) = endian match {
    case 'l' => {
      logger.debug("initializing new client with little endian")
      new Client(socket) with LittleEndian
    }
    case 'B' => {
      logger.debug("initializing new client with big endian")
      new Client(socket) with BigEndian
    }
    case _ => {
      logger.error(s"invalid endian info received: ${endian}")
      throw new ProtocolException(ConnectionError("invalid endian") (java.nio.ByteOrder.LITTLE_ENDIAN))(socket)
    }
  }
}

abstract class Client(handle: IO.SocketHandle) extends Logging {
  import IO._

  implicit val endian: java.nio.ByteOrder
  implicit val socket: SocketHandle = handle

  def handleConnection: Iteratee[Unit] = {
    for {
      connection <- Connection.readConnectionInfo()
    } yield {
      connection match {
        case Connection(_, _, None, None) => {
          socket write Connection.getOkResponse()
          logger.debug("sent ok response for connection")
        }
        case Connection(_, _, _, _) => {
          throw new ProtocolException(ConnectionError("authentication not supported by server"))
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
