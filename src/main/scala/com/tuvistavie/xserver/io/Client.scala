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

  def receive = {
    case Read(handle, bytes) => {
      val client = Client(handle.asSocket, bytes.head.toChar)
      it(Chunk(bytes))
      context become(handleMessages)
      it flatMap ( _ => client.handleConnection )
      it flatMap ( _ => client.handleMessages )
    }
  }

  def handleMessages: Actor.Receive = {
    case Read(socket, bytes) => it(Chunk(bytes))
  }
}

object Client {
  def apply(socket: IO.SocketHandle, endian: Char) = endian match {
    case 'l' => new Client(socket) with LittleEndian
    case 'B' => new Client(socket) with BigEndian
    case _ => throw new ProtocolException(socket,
          new ConnectionError("invalid endian") (java.nio.ByteOrder.LITTLE_ENDIAN)
          )
  }
}

abstract class Client(socket: IO.SocketHandle) {
  import IO._

  implicit val endian: java.nio.ByteOrder

  println(endian)

  def handleConnection: Iteratee[Unit] = {
    for {
      info <- take(12)
      iterator = info.iterator
      byteOrder = iterator.getByte
      _ = iterator.getByte
      majorVersion = iterator.getShort
      minorVersion = iterator.getShort
      n = iterator.getShort
      d = iterator.getShort
    } yield {
      println(majorVersion)
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
