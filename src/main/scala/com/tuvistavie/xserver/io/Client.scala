package com.tuvistavie.xserver.io

import akka.actor.{Actor, IO}

import com.tuvistavie.xserver.protocol.errors.{ BaseError, ConnectionError }
import com.tuvistavie.xserver.util.IntWithPad._
import com.tuvistavie.xserver.util.Properties.{ settings => Config }

class ProtocolException(sender: IO.SocketHandle, error: BaseError) extends RuntimeException {
  sender.write(error.toBytes)
}

class ClientManager extends Actor {
  import IO._

  val it = new IterateeRefAsync(Iteratee.unit)(context.dispatcher)

  private var handle: Option[SocketHandle] = None

  it flatMap { _ => initializeConnection }

  def initializeConnection: Iteratee[Unit] = {
    for {
      a <- take(2)
    } yield {
      val endian = a.iterator.getByte.toChar
      val client: Client = endian match {
        case 'l' => new Client(handle.get) with LittleEndian
        case 'B' => new Client(handle.get) with BigEndian
        case _ => throw new ProtocolException(handle.get,
          new ConnectionError("invalid endian") (java.nio.ByteOrder.LITTLE_ENDIAN)
        )
      }
      it flatMap ( _ => client.handleConnection )
      it flatMap ( _ => client.handleMessages )
    }
  }

  def receive() = {
    case Read(socket, bytes) => {
      if(handle == None) handle = Some(socket.asSocket)
      it(Chunk(bytes))
    }
  }
}

abstract class Client(socket: IO.SocketHandle) {
  import IO._

  implicit val endian: java.nio.ByteOrder

  def handleConnection: Iteratee[Unit] = {
    for {
      info <- take(10)
      iterator = info.iterator
      majorVersion = iterator.getShort
      minorVersion = iterator.getShort
      n = iterator.getShort
      d = iterator.getShort
    } yield {
      if(majorVersion != Config.getInt("protocol.major-version")
        || minorVersion != Config.getInt("protocol.minor-version")) {
        throw new ProtocolException(socket, new ConnectionError("invalid protocol version"))
      }
      if(n != 0 || d != 0) {
        throw new ProtocolException(socket, new ConnectionError("authentication not supported by server"))
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
