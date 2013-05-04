package com.tuvistavie.xserver.protocol

import com.tuvistavie.xserver.io._
import com.tuvistavie.util.Enumerable

package object types {
  type Card8 = UInt8
  type Card16 = UInt16
  type Card32 = UInt32
  type Bitmask = Card32
  type Window = Card32
  type Pixmap = Card32
  type Cursor = Card32
  type Font = Card32
  type GContext = Card32
  type Colormap = Card32
  type Drawable = Card32
  type Fontable = Card32
  type VisualID = Card32
  type Timestamp = Card32
  type Keycode = Card8
  type Button = Card8

  type SetOfEvent = Set[EventMask]

  def generateMaskSet[T](mask: Int, obj: Enumerable[T]): Set[T] = {
    def generate(mask: Int, currentVal: Int, set: Set[T]): Set[T] = {
      if(currentVal >= mask) set
      else if((currentVal & mask) != 0)
        generate(mask, currentVal << 1, set + obj.fromValue(currentVal))
      else
        generate(mask, currentVal << 1, set)
    }
    generate(mask, 0x00, Set())
  }

  private[types] def generateMask[T <: HasValue](mask: Int)(implicit obj: Enumerable[T], unused: T) = {
    val set = generateMaskSet(mask, obj)
    if((mask & unused.value) != 0) set + unused
    else set
  }
}

package types {

  trait HasValue {
    val value: Int
  }

  abstract sealed class Gravity(val value: Int)
  abstract sealed class BitGravity(override val value: Int) extends Gravity(value)
  abstract sealed class WindowGravity(override val value: Int) extends Gravity(value)

  case object Forget extends BitGravity(0)
  case object Unmap extends WindowGravity(0)

  case object NorthWest extends Gravity(1)
  case object North extends Gravity(2)
  case object NorthEast extends Gravity(3)
  case object West extends Gravity(4)
  case object Center extends Gravity(5)
  case object East extends Gravity(6)
  case object SouthWest extends Gravity(7)
  case object South extends Gravity(8)
  case object SouthEast extends Gravity(9)
  case object Static extends Gravity(10)

  private object Gravity {
    private[types] def fromValue(value: Int) = value match {
      case NorthWest.value => NorthWest
      case North.value => North
      case NorthEast.value => NorthEast
      case West.value => West
      case Center.value => Center
      case East.value => East
      case SouthWest.value => SouthWest
      case South.value => South
      case SouthEast.value => SouthEast
      case Static.value => Static
    }
  }

  object BitGravity {
    def fromValue(value: Int) = value match {
      case 0  => Forget
      case _ => Gravity.fromValue(value).asInstanceOf[BitGravity]
    }
  }

  object WindowGravity {
    def fromValue(value: Int) = value match {
      case 0 => Forget
      case _ => Gravity.fromValue(value)
    }
  }

  abstract sealed class EventMask(val value: Int) extends HasValue
  case object KeyPress extends EventMask(0x00000001)
  case object KeyRelease extends EventMask(0x00000002)
  case object ButtonPress extends EventMask(0x00000004)
  case object ButtonRelease extends EventMask(0x00000008)
  case object EnterWindow extends EventMask(0x00000010)
  case object LeaveWindow extends EventMask(0x00000020)
  case object PointerMotion extends EventMask(0x00000040)
  case object PointerMotionHint extends EventMask(0x00000080)
  case object Button1Motion extends EventMask(0x00000100)
  case object Button2Motion extends EventMask(0x00000200)
  case object Button3Motion extends EventMask(0x00000400)
  case object Button4Motion extends EventMask(0x00000800)
  case object Button5Motion extends EventMask(0x00001000)
  case object ButtonMotion extends EventMask(0x00002000)
  case object KeymapState extends EventMask(0x00004000)
  case object Exposure extends EventMask(0x00008000)
  case object VisibilityChange extends EventMask(0x00010000)
  case object StructureNotify extends EventMask(0x00020000)
  case object ResizeRedirect extends EventMask(0x00040000)
  case object SubstructureNotify extends EventMask(0x00080000)
  case object SubstructureRedirect extends EventMask(0x00100000)
  case object FocusChange extends EventMask(0x00200000)
  case object PropertyChange extends EventMask(0x00400000)
  case object ColormapChange extends EventMask(0x00800000)
  case object OwnerGrabButton extends EventMask(0x01000000)

  object EventMask extends Enumerable[EventMask] {
    def fromValue(value: Int) = value match {
      case KeyPress.value => KeyPress
      case KeyRelease.value => KeyRelease
      case ButtonPress.value => ButtonPress
      case ButtonRelease.value => ButtonRelease
      case EnterWindow.value => EnterWindow
      case LeaveWindow.value => LeaveWindow
      case PointerMotion.value => PointerMotion
      case PointerMotionHint.value => PointerMotionHint
      case Button1Motion.value => Button1Motion
      case Button2Motion.value => Button2Motion
      case Button3Motion.value => Button3Motion
      case Button4Motion.value => Button4Motion
      case Button5Motion.value => Button5Motion
      case ButtonMotion.value => ButtonMotion
      case KeymapState.value => KeymapState
      case Exposure.value => Exposure
      case VisibilityChange.value => VisibilityChange
      case StructureNotify.value => StructureNotify
      case ResizeRedirect.value => ResizeRedirect
      case SubstructureNotify.value => SubstructureNotify
      case SubstructureRedirect.value => SubstructureRedirect
      case FocusChange.value => FocusChange
      case PropertyChange.value => PropertyChange
      case ColormapChange.value => ColormapChange
      case OwnerGrabButton.value => OwnerGrabButton
    }
  }

  abstract sealed class NormalEventMask(override val value: Int) extends EventMask(value)

  object NormalEventMask {
    implicit case object Unused extends NormalEventMask(0xfe000000)
    implicit val maskObject = EventMask
    def fromMask(mask: Int) = generateMask(mask)
  }

  abstract sealed class PointerEventMask(override val value: Int) extends EventMask(value)
  object PointerEventMask {
    implicit case object Unused extends NormalEventMask(0xffff8003)
    implicit val maskObject = EventMask
    def fromMask(mask: Int) = generateMask(mask)
  }

  abstract sealed class DeviceEventMask(override val value: Int) extends EventMask(value)

  object DeviceEventMask {
    implicit case object Unused extends DeviceEventMask(0xffffc0b0)
    implicit val maskObject = EventMask
    def fromMask(mask: Int) = generateMask(mask)
  }

  abstract sealed class BaseKeyMask(val value: Int) extends HasValue
  case object Shift extends BaseKeyMask(0x0001)
  case object Lock extends BaseKeyMask(0x0002)
  case object Control extends BaseKeyMask(0x0004)
  case object Mod1 extends BaseKeyMask(0x0008)
  case object Mod2 extends BaseKeyMask(0x0010)
  case object Mod3 extends BaseKeyMask(0x0020)
  case object Mod4 extends BaseKeyMask(0x0040)
  case object Mod5 extends BaseKeyMask(0x0080)
  case object Button1 extends BaseKeyMask(0x0100)
  case object Button2 extends BaseKeyMask(0x0200)
  case object Button3 extends BaseKeyMask(0x0400)
  case object Button4 extends BaseKeyMask(0x0800)
  case object Button5 extends BaseKeyMask(0x1000)

  object BaseKeyMask extends Enumerable[BaseKeyMask] {
    def fromValue(value: Int) = value match {
      case Shift.value => Shift
      case Lock.value => Lock
      case Control.value => Control
      case Mod1.value => Mod1
      case Mod2.value => Mod2
      case Mod3.value => Mod3
      case Mod4.value => Mod4
      case Mod5.value => Mod5
      case Button1.value => Button1
      case Button2.value => Button2
      case Button3.value => Button3
      case Button4.value => Button4
      case Button5.value => Button5
    }
  }

  abstract sealed class KeyButMask(override val value: Int) extends BaseKeyMask(value)
  object KeyButMask {
    implicit case object Unused extends BaseKeyMask(0xe000)
    implicit val obj = BaseKeyMask
    def fromMask(mask: Int) = generateMask(mask)
  }

  abstract sealed class KeyMask(override val value: Int) extends BaseKeyMask(value)
  object KeyMask {
    implicit case object Unused extends BaseKeyMask(0xff00)
    implicit val obj = BaseKeyMask
    def fromMask(mask: Int) = generateMask(mask)
  }

  class Point(val x: Int16, val y: Int16)
  object Point {
    def apply(stream: BinaryInputStream) = {
      val x = stream.readInt16()
      val y = stream.readInt16()
      new Point(x, y)
    }
  }

  class Rectangle(val x: Int16, val y: Int16, val width: Card16, val height: Card16)
  object Rectangle {
    def apply(stream: BinaryInputStream) = {
      val x = stream.readInt16()
      val y = stream.readInt16()
      val width = stream.readUInt16()
      val height = stream.readUInt16()
      new Rectangle(x, y, width, height)
    }
  }

  class Arc(val x: Int16, val y: Int16, val width: Card16, val height: Card16, angle1: Int16, angle2: Int16)
  object Arc {
    def apply(stream: BinaryInputStream) = {
      val x = stream.readInt16()
      val y = stream.readInt16()
      val width = stream.readUInt16()
      val height = stream.readUInt16()
      val angle1 = stream.readInt16()
      val angle2 = stream.readInt16()
      new Arc(x, y, width, height, angle1, angle2)
    }
  }

  class Host(val family: UInt8, val address: String)
  object Host {
    def apply(stream: BinaryInputStream) = {
      val family = stream.readUInt8()
      stream.skip(1)
      val addressLength = stream.readUInt16()
      var address = new Array[Byte](addressLength)
      stream.read(address, 0, addressLength)
      stream.readPad(addressLength)
      new Host(family, new String(address))
    }
  }

  class Str(val value: String)
  object Str {
    def apply(stream: BinaryInputStream) = {
      val n = stream.readUInt8()
      var str = new Array[Byte](n)
      stream.read(str, 0, n)
      stream.readPad(n)
      new Str(new String(str))
    }
  }
}
