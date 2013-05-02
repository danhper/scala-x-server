package com.tuvistavie.xserver.protocol

import scala.collection.mutable

import com.tuvistavie.xserver.protocol._
import com.tuvistavie.xserver.io._

package requests {

  abstract class Request(val opCode: Int8)

  object Request {
    def apply(opCode: Int8, stream: BinaryInputStream) = {
      val data = stream.readInt8()
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
  case class Pixmap(override val value: Int32) extends WindowValue(value)
  case class BackgroundPixel(override val value: Int32) extends WindowValue(value)
  case class BorderPixmap(override val value: Int32) extends WindowValue(value)
  case class BorderPixel(override val value: Int32) extends WindowValue(value)
  case class BitGravity(override val value: Int8) extends WindowValue(value)
  case class WindowGravity(override val value: Int8) extends WindowValue(value)
  case class BackingStore(override val value: Int8) extends WindowValue(value)
  case class BackingPlanes(override val value: Int32) extends WindowValue(value)
  case class BackingPixels(override val value: Int32) extends WindowValue(value)
  case class OverrideRedirect(override val value: Int8) extends WindowValue(value)
  case class SaveUnder(override val value: Int8) extends WindowValue(value)
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
      if((mask & 0x0010) != 0) values += BitGravity(stream.readInt32().toInt8)
      if((mask & 0x0020) != 0) values += WindowGravity(stream.readInt32().toInt8)
      if((mask & 0x0040) != 0) values += BackingStore(stream.readInt32().toInt8)
      if((mask & 0x0080) != 0) values += BackingPlanes(stream.readInt32())
      if((mask & 0x0100) != 0) values += BackingPixels(stream.readInt32())
      if((mask & 0x0200) != 0) values += OverrideRedirect(stream.readInt32().toInt8)
      if((mask & 0x0400) != 0) values += SaveUnder(stream.readInt32().toInt8)
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
    val values: List[WindowValue]
    ) extends Request(1)

  object CreateWindow {
    def apply(stream: BinaryInputStream, depth: Int8) = {
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
        height, borderWidth, windowClass, visualId, values)
    }
  }

  case class ChangeWindowAttributes (
    val window: Int32,
    val values: List[WindowValue]
    ) extends Request(2)

  object ChangeWindowAttributes {
    def apply(stream: BinaryInputStream) = {
      val window = stream.readInt32()
      val bitmask = stream.readInt32()
      val values = WindowValue(stream, bitmask)
      new ChangeWindowAttributes(window, values)
    }
  }

  case class GetWindowAttributes (
    val window: Int32
    ) extends Request(3)

  object GetWindowAttributes {
    def apply(stream: BinaryInputStream) = {
      new GetWindowAttributes(stream.readInt32())
    }
  }

  case class DestroyWindow (
    val window: Int32
    ) extends Request(4)

  object DestroyWindow {
    def apply(stream: BinaryInputStream) = {
      new DestroyWindow(stream.readInt32())
    }
  }

  case class DestroySubWindows (
    val window: Int32
    ) extends Request(5)

  object DestroySubWindows {
    def apply(stream: BinaryInputStream) = {
      new DestroyWindow(stream.readInt32())
    }
  }

  case class ChangeSaveSet (
    val mode: Int8,
    val window: Int32
    ) extends Request(6)

  object ChangeSaveSet {
    def apply(stream: BinaryInputStream, mode: Int8) = {
      new ChangeSaveSet(mode, stream.readInt32())
    }
  }

  case class ReparentWindow (
    val window: Int32,
    val parent: Int32,
    val x: Int16,
    val y: Int16
    ) extends Request(7)

  object ReparentWindow {
    def apply(stream: BinaryInputStream) = {
      val window = stream.readInt32()
      val parent = stream.readInt32()
      val x = stream.readInt16()
      val y = stream.readInt16()
      new ReparentWindow(window, parent, x, y)
    }
  }

  case class MapWindow (
    val window: Int32
    ) extends Request(8)

  object MapWindow {
    def apply(stream: BinaryInputStream) = {
      new MapWindow(stream.readInt32())
    }
  }

  case class MapSubwindows (
    val window: Int32
    ) extends Request(9)

  object MapSubwindows {
    def apply(stream: BinaryInputStream) = {
      new MapSubwindows(stream.readInt32())
    }
  }

  case class UnmapWindow (
    val window: Int32
    ) extends Request(10)

  object UnmapWindow {
    def apply(stream: BinaryInputStream) = {
      new UnmapWindow(stream.readInt32())
    }
  }

  case class UnmapSubwindows (
    val window: Int32
    ) extends Request(11)

  object UnmapSubwindows {
    def apply(stream: BinaryInputStream) = {
      new UnmapSubwindows(stream.readInt32())
    }
  }

  abstract class ConfigureWindowValue(val value: IntValue)
  case class X(override val value: Int16) extends ConfigureWindowValue(value)
  case class Y(override val value: Int16) extends ConfigureWindowValue(value)
  case class Width(override val value: Int16) extends ConfigureWindowValue(value)
  case class Height(override val value: Int16) extends ConfigureWindowValue(value)
  case class BorderWidth(override val value: Int16) extends ConfigureWindowValue(value)
  case class Sibling(override val value: Int32) extends ConfigureWindowValue(value)
  case class StackMode(override val value: Int8) extends ConfigureWindowValue(value)

  object ConfigureWindowValue {
    def apply(stream: BinaryInputStream, mask: Int): List[ConfigureWindowValue] = {
      val values = mutable.MutableList[ConfigureWindowValue]()
      if((mask & 0x0001) != 0) values += X(stream.readInt32().toInt16)
      if((mask & 0x0002) != 0) values += Y(stream.readInt32().toInt16)
      if((mask & 0x0004) != 0) values += Width(stream.readInt32().toInt16)
      if((mask & 0x0008) != 0) values += Height(stream.readInt32().toInt16)
      if((mask & 0x0010) != 0) values += BorderWidth(stream.readInt32().toInt16)
      if((mask & 0x0020) != 0) values += Sibling(stream.readInt32())
      if((mask & 0x0040) != 0) values += StackMode(stream.readInt32().toInt8)
      values.toList
    }
  }

  case class ConfigureWindow (
    val window: Int32,
    val values: List[ConfigureWindowValue]
    ) extends Request(12)

  object ConfigureWindow {
    def apply(stream: BinaryInputStream) = {
      val window = stream.readInt32()
      val mask = stream.readInt16()
      val values = ConfigureWindowValue(stream, mask)
      new ConfigureWindow(window, values)
    }
  }

  case class CirculateWindow (
    val direction: Int8,
    val window: Int32
    ) extends Request(13)

  object CirculateWindow {
    def apply(stream: BinaryInputStream, direction: Int8) = {
      new CirculateWindow(direction, stream.readInt32())
    }
  }

  case class GetGeometry (
    val drawable: Int32
    ) extends Request(14)

  object GetGeometry {
    def apply(stream: BinaryInputStream) = {
      new GetGeometry(stream.readInt32())
    }
  }

  case class QueryTree (
    val window: Int32
    ) extends Request(15)

  object QueryTree {
    def apply(stream: BinaryInputStream) = {
      new QueryTree(stream.readInt32())
    }
  }

  case class InternAtom (
    val onlyIfExists: Int8,
    val name: String
    ) extends Request(16)

  object InternAtom {
    def apply(stream: BinaryInputStream, onlyIfExists: Int8) = {
      val nameLength = stream.readInt16()
      val name = new Array[Byte](nameLength)
      stream.read(name, 0, nameLength)
      stream.readPad(nameLength)
      new InternAtom(onlyIfExists, new String(name))
    }
  }

  case class GetAtomName (
    val atom: Int32
  ) extends Request(17)

  object GetAtomName {
    def apply(stream: BinaryInputStream) = {
      new GetAtomName(stream.readInt32())
    }
  }

  case class ChangeProperty (
    val mode: Int8,
    val window: Int32
  ) extends Request(18)


}
