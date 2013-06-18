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
        case  1 => error.Request(sequenceNumber, minorOpcode, majorOpcode)
        case  2 => error.Value(sequenceNumber, data, minorOpcode, majorOpcode)
        case  3 => error.Window(sequenceNumber, data, minorOpcode, majorOpcode)
        case  4 => error.Pixmap(sequenceNumber, data, minorOpcode, majorOpcode)
        case  5 => error.Atom(sequenceNumber, data, minorOpcode, majorOpcode)
        case  6 => error.Cursor(sequenceNumber, data, minorOpcode, majorOpcode)
        case  7 => error.Font(sequenceNumber, data, minorOpcode, majorOpcode)
        case  8 => error.Match(sequenceNumber, minorOpcode, majorOpcode)
        case  9 => error.Drawable(sequenceNumber, data, minorOpcode, majorOpcode)
        case 10 => error.Access(sequenceNumber, minorOpcode, majorOpcode)
        case 11 => error.Alloc(sequenceNumber, minorOpcode, majorOpcode)
        case 11 => error.Alloc(sequenceNumber, minorOpcode, majorOpcode)
        case 12 => error.Colormap(sequenceNumber, data, minorOpcode, majorOpcode)
        case 13 => error.GContext(sequenceNumber, data, minorOpcode, majorOpcode)
        case 14 => error.IDChoice(sequenceNumber, data, minorOpcode, majorOpcode)
        case 15 => error.Name(sequenceNumber, minorOpcode, majorOpcode)
        case 16 => error.Length(sequenceNumber, minorOpcode, majorOpcode)
        case 17 => error.Implementation(sequenceNumber, minorOpcode, majorOpcode)
        case _ => error.BadError
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
  case object BadError extends XError(0, 0, 0, 0, 0)

  case class Request (
    seqNum: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(1, seqNum, 0, minOpCode, majOpCode)

  case class Value (
    seqNum: Int,
    badValue: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(2, seqNum, badValue, minOpCode, majOpCode)

  case class Window (
    seqNum: Int,
    badResourceId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(3, seqNum, badResourceId, minOpCode, majOpCode)

  case class Pixmap (
    seqNum: Int,
    badResourceId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(4, seqNum, badResourceId, minOpCode, majOpCode)

  case class Atom (
    seqNum: Int,
    badAtomId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(5, seqNum, badAtomId, minOpCode, majOpCode)

  case class Cursor (
    seqNum: Int,
    badResourceId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(6, seqNum, badResourceId, minOpCode, majOpCode)

  case class Font (
    seqNum: Int,
    badResourceId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(7, seqNum, badResourceId, minOpCode, majOpCode)

  case class Match (
    seqNum: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(8, seqNum, 0, minOpCode, majOpCode)

  case class Drawable (
    seqNum: Int,
    badResourceId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(9, seqNum, badResourceId, minOpCode, majOpCode)

  case class Access (
    seqNum: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(10, seqNum, 0, minOpCode, majOpCode)

  case class Alloc (
    seqNum: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(11, seqNum, 0, minOpCode, majOpCode)

  case class Colormap (
    seqNum: Int,
    badResourceId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(12, seqNum, badResourceId, minOpCode, majOpCode)

  case class GContext (
    seqNum: Int,
    badResourceId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(13, seqNum, badResourceId, minOpCode, majOpCode)

  case class IDChoice (
    seqNum: Int,
    badResourceId: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(14, seqNum, badResourceId, minOpCode, majOpCode)

  case class Name (
    seqNum: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(15, seqNum, 0, minOpCode, majOpCode)

  case class Length (
    seqNum: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(16, seqNum, 0, minOpCode, majOpCode)

  case class Implementation (
    seqNum: Int,
    minOpCode: Int,
    majOpCode: Int
  ) extends XError(17, seqNum, 0, minOpCode, majOpCode)
}
