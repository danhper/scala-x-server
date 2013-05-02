package com.tuvistavie.xserver.protocol.requests;

import scala.collection.mutable

import com.tuvistavie.xserver.protocol._
import com.tuvistavie.xserver.io._

abstract class WindowValue(value: Int32)
case class Pixmap(override val value: Int32) extends WindowValue(value)
case class BackgroundPixel(override val value: Int32) extends WindowValue(value)
case class BorderPixmap(override val value: Int32) extends WindowValue(value)
case class BorderPixel(override val value: Int32) extends WindowValue(value)
case class BitGravity(override val value: Int32) extends WindowValue(value)
case class WindowGravity(override val value: Int32) extends WindowValue(value)
case class BackingStore(override val value: Int32) extends WindowValue(value)
case class BackingPlanes(override val value: Int32) extends WindowValue(value)
case class BackingPixels(override val value: Int32) extends WindowValue(value)
case class OverrideRedirect(override val value: Int32) extends WindowValue(value)
case class SaveUnder(override val value: Int32) extends WindowValue(value)
case class EventMask(override val value: Int32) extends WindowValue(value)
case class NoPropagateMask(override val value: Int32) extends WindowValue(value)
case class ColorMap(override val value: Int32) extends WindowValue(value)
case class Cursor(override val value: Int32) extends WindowValue(value)

object WindowValue {
  def apply(stream: BinaryInputStream, mask: Int): List[WindowValue] = {
    val values = mutable.MutableList[WindowValue]()
    if((mask & 0x0001) != 0) values += Pixmap(stream.readInt32())
    if((mask & 0x0002) != 0) values += BackgroundPixel(stream.readInt32())
    if((mask & 0x0004) != 0) values += BorderPixmap(stream.readInt32())
    if((mask & 0x0008) != 0) values += BorderPixel(stream.readInt32())
    if((mask & 0x0010) != 0) values += BitGravity(stream.readInt32())
    if((mask & 0x0020) != 0) values += WindowGravity(stream.readInt32())
    if((mask & 0x0040) != 0) values += BackingStore(stream.readInt32())
    if((mask & 0x0080) != 0) values += BackingPlanes(stream.readInt32())
    if((mask & 0x0100) != 0) values += BackingPixels(stream.readInt32())
    if((mask & 0x0200) != 0) values += OverrideRedirect(stream.readInt32())
    if((mask & 0x0400) != 0) values += SaveUnder(stream.readInt32())
    if((mask & 0x0800) != 0) values += EventMask(stream.readInt32())
    if((mask & 0x1000) != 0) values += NoPropagateMask(stream.readInt32())
    if((mask & 0x2000) != 0) values += ColorMap(stream.readInt32())
    if((mask & 0x4000) != 0) values += Cursor(stream.readInt32())
    values.toList
  }
}

case class CreateWindow(
  val depth: Int8,
  val windowId: Int32,
  val parent: Int32,
  val x: Int16,
  val y: Int16,
  val width: Int16,
  val height: Int16,
  val borderWidth: Int16,
  val windowClass: Int16,
  val visualId: Int32,
  val bitmask: Int32,
  val values: List[WindowValue]
) extends AbstractRequest(1)

object CreateWindow {
  def apply(stream: BinaryInputStream) = {
    val depth = stream.readInt8()
    val length = stream.readInt16()
    val windowId = stream.readInt32()
    val parent = stream.readInt32()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val width = stream.readInt16()
    val height = stream.readInt16()
    val borderWidth = stream.readInt16()
    val windowClass = stream.readInt16()
    val visualId = stream.readInt32()
    val bitmask = stream.readInt32()
    val values =  WindowValue(stream, bitmask)
    new CreateWindow(depth, windowId, parent, x, y, width,
      height, borderWidth, windowClass, visualId, bitmask, values)
  }
}

case class ChangeWindowAttributes(
  val window: Int32,
  val bitmask: Int32,
  val values: List[WindowValue]
) extends AbstractRequest(2)

object ChangeWindowAttributes {

}

