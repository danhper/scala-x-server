package com.tuvistavie.xserver.protocol.errors

import akka.util.ByteString

import com.tuvistavie.xserver.protocol.ByteSerializable
import com.tuvistavie.xserver.io._
import com.tuvistavie.xserver.protocol.types._

import com.tuvistavie.xserver.util.IntWithPad._
import com.tuvistavie.xserver.util.ExtendedByteStringBuilder._
import com.tuvistavie.xserver.util.Properties.{settings => Config}

abstract class BaseError extends ByteSerializable

case class ConnectionError (
  message: String
) (
  implicit val endian: java.nio.ByteOrder
) extends BaseError {
  def toBytes = {
    var frameBuilder = ByteString.newBuilder
    val n = message.length
    frameBuilder.putByte(n.toByte)
    frameBuilder.putShort(Config.getInt("server.protocol.major-version"))
    frameBuilder.putShort(Config.getInt("server.protocol.minor-version"))
    frameBuilder.putShort((n.withPadding) / 4)
    frameBuilder.putBytes(message.getBytes)
    frameBuilder.writePadding(n)
    frameBuilder result
  }
}

abstract class Error (
  val code: Card8,
  val sequenceNumber: Card16,
  val badValue: Option[Card32],
  val minorOpCode: Card16,
  val majorOpCode: Card8
)

object ErrorFactory {
  def makeError[A](stream: BinaryInputStream, factory: (Card16, Option[Card32], Card16, Card8) => A) = {
    val sequenceNumber = stream.readCard16()
    val badValue = stream.readCard32()
    val minorOpCode = stream.readCard16()
    val majorOpCode = stream.readCard8()
    stream.skipBytes(21)
    factory(sequenceNumber, Some(badValue), minorOpCode, majorOpCode)
  }

  def makeError[A](stream: BinaryInputStream, factory: (Card16, Card16, Card8) => A) = {
    val sequenceNumber = stream.readCard16()
    stream.skipBytes(4)
    val minorOpCode = stream.readCard16()
    val majorOpCode = stream.readCard8()
    stream.skipBytes(21)
    factory(sequenceNumber, minorOpCode, majorOpCode)
  }

}

case class Request (
  override val sequenceNumber: Card16,
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(1, sequenceNumber, None, minorOpCode, majorOpCode)

object Request {
  def apply(stream: BinaryInputStream): Request = ErrorFactory.makeError(stream, Request.apply(_, _, _))
}

case class Value (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(2, sequenceNumber, badValue, minorOpCode, majorOpCode)

object Value {
  def apply(stream: BinaryInputStream): Value = ErrorFactory.makeError(stream, Value.apply(_, _, _, _))
}

case class Window (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(3, sequenceNumber, badValue, minorOpCode, majorOpCode)

object Window {
  def apply(stream: BinaryInputStream): Window = ErrorFactory.makeError(stream, Window.apply(_, _, _, _))
}

case class Pixmap (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(4, sequenceNumber, badValue, minorOpCode, majorOpCode)

object Pixmap {
  def apply(stream: BinaryInputStream): Pixmap = ErrorFactory.makeError(stream, Pixmap.apply(_, _, _, _))
}

case class Atom (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(5, sequenceNumber, badValue, minorOpCode, majorOpCode)

object Atom {
  def apply(stream: BinaryInputStream): Atom = ErrorFactory.makeError(stream, Atom.apply(_, _, _, _))
}

case class Cursor (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(6, sequenceNumber, badValue, minorOpCode, majorOpCode)

object Cursor {
  def apply(stream: BinaryInputStream): Cursor = ErrorFactory.makeError(stream, Cursor.apply(_, _, _, _))
}

case class Font (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(7, sequenceNumber, badValue, minorOpCode, majorOpCode)

object Font {
  def apply(stream: BinaryInputStream): Font = ErrorFactory.makeError(stream, Font.apply(_, _, _, _))
}

case class Match (
  override val sequenceNumber: Card16,
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(8, sequenceNumber, None, minorOpCode, majorOpCode)

object Match {
  def apply(stream: BinaryInputStream): Match = ErrorFactory.makeError(stream, Match.apply(_, _, _))
}

case class Drawable (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(9, sequenceNumber, badValue, minorOpCode, majorOpCode)

object Drawable {
  def apply(stream: BinaryInputStream): Drawable = ErrorFactory.makeError(stream, Drawable.apply(_, _, _, _))
}

case class Access (
  override val sequenceNumber: Card16,
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(10, sequenceNumber, None, minorOpCode, majorOpCode)

object Access {
  def apply(stream: BinaryInputStream): Access = ErrorFactory.makeError(stream, Access.apply(_, _, _))
}

case class Alloc (
  override val sequenceNumber: Card16,
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(11, sequenceNumber, None, minorOpCode, majorOpCode)

object Alloc {
  def apply(stream: BinaryInputStream): Alloc = ErrorFactory.makeError(stream, Alloc.apply(_, _, _))
}

case class Colormap (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(12, sequenceNumber, badValue, minorOpCode, majorOpCode)

object Colormap {
  def apply(stream: BinaryInputStream): Colormap = ErrorFactory.makeError(stream, Colormap.apply(_, _, _, _))
}

case class GContext (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(13, sequenceNumber, badValue, minorOpCode, majorOpCode)

object GContext {
  def apply(stream: BinaryInputStream): GContext = ErrorFactory.makeError(stream, GContext.apply(_, _, _, _))
}

case class IDChoice (
  override val sequenceNumber: Card16,
  override val badValue: Option[Card32],
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(14, sequenceNumber, badValue, minorOpCode, majorOpCode)

object IDChoice {
  def apply(stream: BinaryInputStream): IDChoice = ErrorFactory.makeError(stream, IDChoice.apply(_, _, _, _))
}

case class Name (
  override val sequenceNumber: Card16,
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(15, sequenceNumber, None, minorOpCode, majorOpCode)

object Name {
  def apply(stream: BinaryInputStream): Name = ErrorFactory.makeError(stream, Name.apply(_, _, _))
}

case class Length (
  override val sequenceNumber: Card16,
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(15, sequenceNumber, None, minorOpCode, majorOpCode)

object Length {
  def apply(stream: BinaryInputStream): Length = ErrorFactory.makeError(stream, Length.apply(_, _, _))
}

case class Implementation (
  override val sequenceNumber: Card16,
  override val minorOpCode: Card16,
  override val majorOpCode: Card8
) extends Error(15, sequenceNumber, None, minorOpCode, majorOpCode)

object Implementation {
  def apply(stream: BinaryInputStream): Implementation = ErrorFactory.makeError(stream, Implementation.apply(_, _, _))
}
