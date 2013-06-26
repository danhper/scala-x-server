package com.tuvistavie.xserver.protocol.request

import java.nio.ByteOrder
import akka.actor.IO
import akka.util.ByteIterator
import scala.collection.mutable.ListBuffer
import com.tuvistavie.xserver.backend.util.{ ExtendedByteIterator, Conversions }
import ExtendedByteIterator._
import com.typesafe.scalalogging.slf4j.Logging

trait TextItem8

object TextItem8 {
  def fromByteIterator(iterator: ByteIterator)(implicit endian: java.nio.ByteOrder): TextItem8 = {
    val length = iterator.getByte
    if(length == 255) TextItem8Font(iterator getInt ByteOrder.BIG_ENDIAN)
    else TextItem8Str(iterator getByte, iterator getString length)
  }
}

case class TextItem8Str (
  delta: Int,
  text: String
) extends TextItem8


case class TextItem8Font (
  font: Int
) extends TextItem8


case class PolyText8 (
  drawable: Int,
  context: Int,
  x: Int,
  y: Int,
  textItems: List[TextItem8]
) extends Request
with NeedsTransfer {
  val opCode = 74
}

object PolyText8 extends RequestGenerator {
  override def parseRequest(iterator: ByteIterator, data: Int)(implicit endian: java.nio.ByteOrder) = {
    val drawable = iterator getInt
    val context = iterator getInt
    val (x, y) = (iterator getShort, iterator getShort)
    val items = new ListBuffer[TextItem8]()
    while(iterator.len >= 2) items += TextItem8.fromByteIterator(iterator)
    PolyText8(drawable, context, x, y, items result)
  }
}
