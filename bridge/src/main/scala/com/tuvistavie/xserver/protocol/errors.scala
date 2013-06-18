package com.tuvistavie.xserver.protocol

import akka.util.ByteString
import akka.actor.IO
import com.typesafe.scalalogging.slf4j.Logging

import com.tuvistavie.xserver.backend.util.{ ExtendedByteStringBuilder, ExtendedByteIterator, Conversions }

object XError extends Logging {
  import IO._
  import ExtendedByteIterator._

  def getError()(implicit endian: java.nio.ByteOrder) = {
    logger.debug("starting to parse error")
    for {
      errorContent <- take(31)
      iterator = errorContent.iterator
      code = iterator.getByte
      _ = logger.debug("handling error with code ${code}")
      sequenceNumber = iterator.getShort
      data = iterator.getInt
      minorOpcode = iterator.getShort
      majorOpcode = iterator.getByte
      _ = iterator.skip(21)
    } yield {
      code match {
        case 1 => error.Request(sequenceNumber, minorOpcode, majorOpcode)
      }
    }
  }
}


abstract class XError (
  val code: Int,
  val sequenceNumber: Int,
  val errorContent: Int,
  val minorOpcode: Int,
  val majorOpcode: Int
) {
  import Conversions._
  import ExtendedByteStringBuilder._

  def toBytes(implicit endian: java.nio.ByteOrder) = {
    val builder = ByteString.newBuilder
    builder putByte  0
    builder putByte  code
    builder putShort sequenceNumber
    builder putInt   errorContent
    builder putShort minorOpcode
    builder putByte  majorOpcode
    builder fill     21
    builder result
  }
}

package error {
  case class Request (
    override val sequenceNumber: Int,
    override val minorOpcode: Int,
    override val majorOpcode: Int
  ) extends XError(1, sequenceNumber, 0, minorOpcode, majorOpcode)
}
