package com.tuvistavie.xserver.protocol.error

import akka.util.ByteString
import akka.actor.IO
import com.typesafe.scalalogging.slf4j.Logging

import com.tuvistavie.xserver.backend.util.{ ExtendedByteStringBuilder, ExtendedByteIterator, Conversions }


trait ErrorLike

class ProtocolException (
  error: ErrorLike
) extends RuntimeException

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
        case  1 => RequestError(sequenceNumber, minorOpcode, majorOpcode)
        case  2 => ValueError(sequenceNumber, data, minorOpcode, majorOpcode)
        case  3 => WindowError(sequenceNumber, data, minorOpcode, majorOpcode)
        case  4 => PixmapError(sequenceNumber, data, minorOpcode, majorOpcode)
        case  5 => AtomError(sequenceNumber, data, minorOpcode, majorOpcode)
        case  6 => CursorError(sequenceNumber, data, minorOpcode, majorOpcode)
        case  7 => FontError(sequenceNumber, data, minorOpcode, majorOpcode)
        case  8 => MatchError(sequenceNumber, minorOpcode, majorOpcode)
        case  9 => DrawableError(sequenceNumber, data, minorOpcode, majorOpcode)
        case 10 => AccessError(sequenceNumber, minorOpcode, majorOpcode)
        case 11 => AllocError(sequenceNumber, minorOpcode, majorOpcode)
        case 12 => ColormapError(sequenceNumber, data, minorOpcode, majorOpcode)
        case 13 => GContextError(sequenceNumber, data, minorOpcode, majorOpcode)
        case 14 => IDChoiceError(sequenceNumber, data, minorOpcode, majorOpcode)
        case 15 => NameError(sequenceNumber, minorOpcode, majorOpcode)
        case 16 => LengthError(sequenceNumber, minorOpcode, majorOpcode)
        case 17 => ImplementationError(sequenceNumber, minorOpcode, majorOpcode)
        case _ => BadError
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
) extends ErrorLike {
  import Conversions._
  import ExtendedByteStringBuilder._

  def toByteString(implicit endian: java.nio.ByteOrder) = {
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


case object BadError extends XError(0, 0, 0, 0, 0)

case class RequestError (
  seqNum: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(1, seqNum, 0, minOpCode, majOpCode)

case class ValueError (
  seqNum: Int,
  badValue: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(2, seqNum, badValue, minOpCode, majOpCode)

case class WindowError (
  seqNum: Int,
  badResourceId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(3, seqNum, badResourceId, minOpCode, majOpCode)

case class PixmapError (
  seqNum: Int,
  badResourceId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(4, seqNum, badResourceId, minOpCode, majOpCode)

case class AtomError (
  seqNum: Int,
  badAtomId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(5, seqNum, badAtomId, minOpCode, majOpCode)

case class CursorError (
  seqNum: Int,
  badResourceId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(6, seqNum, badResourceId, minOpCode, majOpCode)

case class FontError (
  seqNum: Int,
  badResourceId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(7, seqNum, badResourceId, minOpCode, majOpCode)

case class MatchError (
  seqNum: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(8, seqNum, 0, minOpCode, majOpCode)

case class DrawableError (
  seqNum: Int,
  badResourceId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(9, seqNum, badResourceId, minOpCode, majOpCode)

case class AccessError (
  seqNum: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(10, seqNum, 0, minOpCode, majOpCode)

case class AllocError (
  seqNum: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(11, seqNum, 0, minOpCode, majOpCode)

case class ColormapError (
  seqNum: Int,
  badResourceId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(12, seqNum, badResourceId, minOpCode, majOpCode)

case class GContextError (
  seqNum: Int,
  badResourceId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(13, seqNum, badResourceId, minOpCode, majOpCode)

case class IDChoiceError (
  seqNum: Int,
  badResourceId: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(14, seqNum, badResourceId, minOpCode, majOpCode)

case class NameError (
  seqNum: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(15, seqNum, 0, minOpCode, majOpCode)

case class LengthError (
  seqNum: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(16, seqNum, 0, minOpCode, majOpCode)

case class ImplementationError (
  seqNum: Int,
  minOpCode: Int,
  majOpCode: Int
) extends XError(17, seqNum, 0, minOpCode, majOpCode)
