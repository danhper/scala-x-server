package com.tuvistavie.xserver.io

import akka.actor.{Actor, IO}

import com.tuvistavie.xserver.protocol.errors.{BaseError, ConnectionError}

class ProtocolException(sender: IO.SocketHandle, error: BaseError) extends RuntimeException {
  sender.write(error.toBytes)
}

class ClientManager extends Actor {
  import IO._

  val it = new IterateeRefAsync(Iteratee.unit)(context.dispatcher)

  private var handle: Option[SocketHandle] = None

  it flatMap { _ => handleConnection }

  def handleConnection: Iteratee[Unit] = {
    for {
      a <- take(2)
    } yield {
      val endian = a.iterator.getByte.toChar
      val client: Client = endian match {
        case 'l' => new Client(handle.get) with LittleEndian
        case 'B' => new Client(handle.get) with BigEndian
        case _ => throw new ProtocolException(handle.get,
          new ConnectionError(1, 11, 0) with LittleEndian
        )
      }
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

class Client(socket: IO.SocketHandle) {
  import IO._

  def handleMessages: Iteratee[Unit] = repeat {
    for {
      a <- take(1)
    } yield {
      println(a)
    }
  }
}
