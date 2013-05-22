package com.tuvistavie.xserver.io

import akka.actor.{Actor, IO}

import com.tuvistavie.xserver.protocol.errors.{BaseError, ConnectionError}

class ProtocolException(sender: IO.SocketHandle, error: BaseError) extends RuntimeException {
  sender.write(error.toBytes)
}

class ClientConnectionManager(socket: IO.SocketHandle) extends Actor {
  import IO._

  val it = new IterateeRefAsync(Iteratee.unit)(context.dispatcher)

  it flatMap { _ => handleConnection }

  def handleConnection: Iteratee[Unit] = {
    for {
      a <- take(2)
    } yield {
      val endian = a.iterator.getByte.toChar
      val client: Client = endian match {
        case 'l' => new Client(socket) with LittleEndian
        case 'B' => new Client(socket) with BigEndian
        case _ => throw new ProtocolException(socket,
          new ConnectionError(1, 11, 0) {
            val byteOrder = BigEndian
          }
        )
      }
      it flatMap ( _ => client.handleMessages )
    }
  }

  def receive() = {
    case Read(socket, bytes) => {
      it(Chunk(bytes))
    }
  }
}
