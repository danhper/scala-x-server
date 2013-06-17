package com.tuvistavie.xserver.protocol

import akka.actor.IO
import com.typesafe.scalalogging.slf4j.Logging
import com.tuvistavie.xserver.backend.util.{ ExtendedByteIterator, Conversions }


abstract class Request (
  val opCode: Int
)

trait RequestGenerator {
  def parseRequest(length: Int, data: Int)(implicit endian: java.nio.ByteOrder): IO.Iteratee[Request]
}

object Request extends Logging {
  import IO._
  import request._

  def getRequest(opCode: Int)(implicit endian: java.nio.ByteOrder, socket: SocketHandle): Iteratee[Request] = {
    for {
      header <- take(3)
      iterator = header.iterator
      data = iterator.getByte
      requestLength = iterator.getShort
      request <- generators(opCode).parseRequest(requestLength, data)
    } yield {
      logger.debug(s"handling request with opcode ${opCode} and length ${requestLength}")
      request
    }
  }

  val generators: Map[Int, RequestGenerator] = Map(
    98 -> QueryExtension
  )
}

package request {
  import ExtendedByteIterator._
  import Conversions._
  import IO._

  case object BadRequest extends Request(0)

  case class QueryExtension (
    val name: String
  ) extends Request(98)

  object QueryExtension extends RequestGenerator {
    override def parseRequest(length: Int, data: Int)(implicit endian: java.nio.ByteOrder) = {
      for {
        request <- take(length)
        iterator = request.iterator
        n = iterator.getShort.toInt
        _ = iterator.skip(2)
        name = iterator.getString(n)
        _ = iterator.skip(n.padding)
      } yield {
        QueryExtension(name)
      }
    }
  }
}
