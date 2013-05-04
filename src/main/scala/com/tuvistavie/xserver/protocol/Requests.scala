package com.tuvistavie.xserver.protocol.requests

import scala.collection.mutable
import com.tuvistavie.util.IntTimes._
import com.tuvistavie.xserver.io._
import com.tuvistavie.xserver.protocol.types._

abstract class Request(val opCode: Int8)

object Request {
  def apply(opCode: Int8, stream: BinaryInputStream) = {
    val data = stream.readUInt8()
    opCode value match {
      case 1 => CreateWindow(stream, data)
      case 2 => ChangeWindowAttributes(stream)
      case 3 => GetWindowAttributes(stream)
      case 4 => DestroyWindow(stream)
      case 5 => DestroySubWindows(stream)
      case 6 => ChangeSaveSet(stream, data)
      case 7 => ReparentWindow(stream)
      case 8 => MapWindow(stream)
      case 9 => MapSubwindows(stream)
      case 10 => UnmapWindow(stream)
      case 11 => UnmapSubwindows(stream)
      case 12 => ConfigureWindow(stream)
    }
  }
}

abstract class WindowValue(val value: IntValue)
case class BackgroundPixmap(override val value: Pixmap) extends WindowValue(value)
case class BackgroundPixel(override val value: Card32) extends WindowValue(value)
case class BorderPixmap(override val value: Pixmap) extends WindowValue(value)
case class BorderPixel(override val value: Card32) extends WindowValue(value)
case class BitGravity(override val value: UInt8) extends WindowValue(value)
case class WindowGravity(override val value: UInt8) extends WindowValue(value)
case class BackingStore(override val value: UInt8) extends WindowValue(value)
case class BackingPlanes(override val value: Int32) extends WindowValue(value)
case class BackingPixels(override val value: Int32) extends WindowValue(value)
case class OverrideRedirect(override val value: UInt8) extends WindowValue(value)
case class SaveUnder(override val value: UInt8) extends WindowValue(value)
case class EventMask(override val value: Int32) extends WindowValue(value)
case class NoPropagateMask(override val value: Int32) extends WindowValue(value)
case class ColorMap(override val value: Int32) extends WindowValue(value)
case class Cursor(override val value: Int32) extends WindowValue(value)

object WindowValue {
  def apply(stream: BinaryInputStream, mask: Int): List[WindowValue] = {
    val values = mutable.MutableList[WindowValue]()
    if((mask & 0x0001) != 0) values += BackgroundPixmap(stream.readUInt32())
    if((mask & 0x0002) != 0) values += BackgroundPixel(stream.readUInt32())
    if((mask & 0x0004) != 0) values += BorderPixmap(stream.readUInt32())
    if((mask & 0x0008) != 0) values += BorderPixel(stream.readUInt32())
    if((mask & 0x0010) != 0) values += BitGravity(stream.readInt32().toUInt8)
    if((mask & 0x0020) != 0) values += WindowGravity(stream.readInt32().toUInt8)
    if((mask & 0x0040) != 0) values += BackingStore(stream.readInt32().toUInt8)
    if((mask & 0x0080) != 0) values += BackingPlanes(stream.readInt32())
    if((mask & 0x0100) != 0) values += BackingPixels(stream.readInt32())
    if((mask & 0x0200) != 0) values += OverrideRedirect(stream.readInt32().toUInt8)
    if((mask & 0x0400) != 0) values += SaveUnder(stream.readInt32().toUInt8)
    if((mask & 0x0800) != 0) values += EventMask(stream.readInt32())
    if((mask & 0x1000) != 0) values += NoPropagateMask(stream.readInt32())
    if((mask & 0x2000) != 0) values += ColorMap(stream.readInt32())
    if((mask & 0x4000) != 0) values += Cursor(stream.readInt32())
    values.toList
  }
}

case class CreateWindow(
  val depth: Card8,
  val window: Window,
  val parent: Window,
  val x: Int16,
  val y: Int16,
  val width: Card16,
  val height: Card16,
  val borderWidth: Card16,
  val windowClass: UInt16,
  val visualId: VisualID,
  val values: List[WindowValue]
  ) extends Request(1)

object CreateWindow {
  def apply(stream: BinaryInputStream, depth: Card8) = {
    val windowId = stream.readUInt32()
    val parent = stream.readUInt32()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val width = stream.readUInt16()
    val height = stream.readUInt16()
    val borderWidth = stream.readUInt16()
    val windowClass = stream.readUInt16()
    val visualId = stream.readUInt32()
    val bitmask = stream.readUInt32()
    val values =  WindowValue(stream, bitmask)
    new CreateWindow(depth, windowId, parent, x, y, width,
      height, borderWidth, windowClass, visualId, values)
  }
}

case class ChangeWindowAttributes (
  val window: Window,
  val values: List[WindowValue]
  ) extends Request(2)

object ChangeWindowAttributes {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readUInt32()
    val bitmask = stream.readUInt32()
    val values = WindowValue(stream, bitmask)
    new ChangeWindowAttributes(window, values)
  }
}

case class GetWindowAttributes (
  val window: Window
  ) extends Request(3)

object GetWindowAttributes {
  def apply(stream: BinaryInputStream) = {
    new GetWindowAttributes(stream.readUInt32())
  }
}

case class DestroyWindow (
  val window: Window
  ) extends Request(4)

object DestroyWindow {
  def apply(stream: BinaryInputStream) = {
    new DestroyWindow(stream.readUInt32())
  }
}

case class DestroySubWindows (
  val window: Window
  ) extends Request(5)

object DestroySubWindows {
  def apply(stream: BinaryInputStream) = {
    new DestroySubWindows(stream.readUInt32())
  }
}

case class ChangeSaveSet (
  val mode: UInt8,
  val window: Window
  ) extends Request(6)

object ChangeSaveSet {
  def apply(stream: BinaryInputStream, mode: UInt8) = {
    new ChangeSaveSet(mode, stream.readUInt32())
  }
}

case class ReparentWindow (
  val window: Window,
  val parent: Window,
  val x: Int16,
  val y: Int16
  ) extends Request(7)

object ReparentWindow {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readUInt32()
    val parent = stream.readUInt32()
    val x = stream.readInt16()
    val y = stream.readInt16()
    new ReparentWindow(window, parent, x, y)
  }
}

case class MapWindow (
  val window: Window
  ) extends Request(8)

object MapWindow {
  def apply(stream: BinaryInputStream) = {
    new MapWindow(stream.readUInt32())
  }
}

case class MapSubwindows (
  val window: Window
  ) extends Request(9)

object MapSubwindows {
  def apply(stream: BinaryInputStream) = {
    new MapSubwindows(stream.readUInt32())
  }
}

case class UnmapWindow (
  val window: Window
  ) extends Request(10)

object UnmapWindow {
  def apply(stream: BinaryInputStream) = {
    new UnmapWindow(stream.readUInt32())
  }
}

case class UnmapSubwindows (
  val window: Window
  ) extends Request(11)

object UnmapSubwindows {
  def apply(stream: BinaryInputStream) = {
    new UnmapSubwindows(stream.readUInt32())
  }
}

abstract class ConfigureWindowValue(val value: IntValue)
case class X(override val value: Int16) extends ConfigureWindowValue(value)
case class Y(override val value: Int16) extends ConfigureWindowValue(value)
case class Width(override val value: Card16) extends ConfigureWindowValue(value)
case class Height(override val value: Card16) extends ConfigureWindowValue(value)
case class BorderWidth(override val value: Card16) extends ConfigureWindowValue(value)
case class Sibling(override val value: Window) extends ConfigureWindowValue(value)
case class StackMode(override val value: UInt8) extends ConfigureWindowValue(value)

object ConfigureWindowValue {
  def apply(stream: BinaryInputStream, mask: Int): List[ConfigureWindowValue] = {
    val values = mutable.MutableList[ConfigureWindowValue]()
    if((mask & 0x0001) != 0) values += X(stream.readInt32().value)
    if((mask & 0x0002) != 0) values += Y(stream.readInt32().value)
    if((mask & 0x0004) != 0) values += Width(stream.readInt32().toUInt16)
    if((mask & 0x0008) != 0) values += Height(stream.readInt32().toUInt16)
    if((mask & 0x0010) != 0) values += BorderWidth(stream.readInt32().toUInt16)
    if((mask & 0x0020) != 0) values += Sibling(stream.readUInt32())
    if((mask & 0x0040) != 0) values += StackMode(stream.readInt32().toUInt8)
    values.toList
  }
}

case class ConfigureWindow (
  val window: Window,
  val values: List[ConfigureWindowValue]
  ) extends Request(12)

object ConfigureWindow {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readUInt32()
    val mask = stream.readUInt16()
    val values = ConfigureWindowValue(stream, mask)
    new ConfigureWindow(window, values)
  }
}

case class CirculateWindow (
  val direction: UInt8,
  val window: Window
  ) extends Request(13)

object CirculateWindow {
  def apply(stream: BinaryInputStream, direction: UInt8) = {
    new CirculateWindow(direction, stream.readUInt32())
  }
}

case class GetGeometry (
  val drawable: Drawable
  ) extends Request(14)

object GetGeometry {
  def apply(stream: BinaryInputStream) = {
    new GetGeometry(stream.readUInt32())
  }
}

case class QueryTree (
  val window: Window
  ) extends Request(15)

object QueryTree {
  def apply(stream: BinaryInputStream) = {
    new QueryTree(stream.readUInt32())
  }
}

case class InternAtom (
  val onlyIfExists: Bool,
  val name: String
  ) extends Request(16)

object InternAtom {
  def apply(stream: BinaryInputStream, onlyIfExists: Bool) = {
    val nameLength = stream.readInt16()
    val name = new Array[Byte](nameLength)
    stream.read(name, 0, nameLength)
    stream.readPad(nameLength)
    new InternAtom(onlyIfExists, new String(name))
  }
}

case class GetAtomName (
  val atom: Atom
  ) extends Request(17)

object GetAtomName {
  def apply(stream: BinaryInputStream) = {
    new GetAtomName(stream.readUInt32())
  }
}

case class ChangeProperty (
  val mode: UInt8,
  val window: Window,
  val property: Atom,
  val propertyType: Atom,
  val format: Card8,
  val data: List[UInt8]
  ) extends Request(18)

object ChangeProperty {
  def apply(stream: BinaryInputStream, mode: UInt8) = {
    val window = stream.readUInt32()
    val property = stream.readUInt32()
    val propertyType = stream.readUInt32()
    val format = stream.readUInt8()
    stream.skip(3)
    val n = stream.readUInt32() * (format / 8)
    val data = mutable.MutableList[UInt8]()
    n times { data += stream.readUInt8() }
    stream.readPad(n)
    new ChangeProperty(mode, window, property, propertyType, format, data.toList)
  }
}

case class DeleteProperty (
  val window: Window,
  val property: Atom
  ) extends Request(19)

object DeleteProperty {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readUInt32()
    val property = stream.readUInt32()
    new DeleteProperty(window, property)
  }
}

case class GetProperty(
  val delete: Bool,
  val window: Window,
  val property: Atom,
  val propertyType: Atom,
  val longOffset: Card32,
  val longLength: Card32
  ) extends Request(20)

object GetProperty {
  def apply(stream: BinaryInputStream, delete: Bool) = {
    val window = stream.readUInt32()
    val property = stream.readUInt32()
    val propertyType = stream.readUInt32()
    val longOffset = stream.readUInt32()
    val longLength = stream.readUInt32()
    new GetProperty(delete, window, property, propertyType, longOffset, longLength)
  }
}

case class ListProperties (
  val window: Window
  ) extends Request(21)

object ListProperties {
  def apply(stream: BinaryInputStream) = {
    new ListProperties(stream.readUInt32())
  }
}

case class SetSelectionOwner (
  val owner: Window,
  val selection: Atom,
  val time: Timestamp
) extends Request(22)

object SetSelectionOwner {
  def apply(stream: BinaryInputStream) = {
    val owner = stream.readUInt32()
    val selection = stream.readUInt32()
    val time = stream.readUInt32()
    new SetSelectionOwner(owner, selection, time)
  }
}


case class GetSelectionOwner (
  val selection: Atom
  ) extends Request(23)

object GetSelectionOwner {
  def apply(stream: BinaryInputStream) = {
    new GetSelectionOwner(stream.readUInt32())
  }
}

case class ConvertSelection (
  val window: Window,
  val selection: Atom,
  val target: Atom,
  val property: Atom,
  val time: Timestamp
) extends Request(24)

object ConvertSelection {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readUInt32()
    val selection = stream.readUInt32()
    val target = stream.readUInt32()
    val property = stream.readUInt32()
    val time = stream.readUInt32()
    new ConvertSelection(window, selection, target, property, time)
  }
}

case class SendEvent (
  val propagate: Bool,
  val window: Window,
  val eventMask: Int32
  // val event: Event
)



