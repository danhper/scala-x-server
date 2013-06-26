package com.tuvistavie.xserver.protocol.request

import akka.actor.IO
import akka.util.ByteIterator
import scala.collection.mutable.ListBuffer

case class Rectangle (
  x: Int,
  y: Int,
  width: Int,
  height: Int
)

object Rectangle {
  def apply(iterator: ByteIterator)(implicit endian: java.nio.ByteOrder): Rectangle = {
    val x = iterator getShort
    val y = iterator getShort
    val width = iterator getShort
    val height = iterator.getShort
    Rectangle(x, y, width, height)
  }
}

case class PolyFillRectangle (
  drawable: Int,
  context: Int,
  rectangles: List[Rectangle]
) extends Request
with NeedsTransfer {
  val opCode = 70
}

object PolyFillRectangle extends RequestGenerator {
  override def parseRequest(iterator: ByteIterator, data: Int)(implicit endian: java.nio.ByteOrder) = {
    val drawable = iterator getInt
    val context = iterator getInt
    val rectangles: ListBuffer[Rectangle] = new ListBuffer()
    while(iterator.nonEmpty) rectangles += Rectangle(iterator)
    PolyFillRectangle(drawable, context, rectangles result)
  }
}
