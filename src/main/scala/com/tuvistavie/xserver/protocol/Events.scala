package com.tuvistavie.xserver.protocol.events

import com.tuvistavie.xserver.protocol.types._

import com.tuvistavie.xserver.io._

abstract class Event(val code: Int8)

case class KeyPress (
  val keycode: Keycode,
  val sequenceNumber: Card16,
  val time: Timestamp,
  val root: Window,
  val event: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val eventX: Int16,
  val eventY: Int16,
  val state: Set[BaseKeyMask],
  val sameScreen: Boolean
) extends Event(2)

case class KeyRelease (
  val keycode: Keycode,
  val sequenceNumber: Card16,
  val time: Timestamp,
  val root: Window,
  val event: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val eventX: Int16,
  val eventY: Int16,
  val state: Set[BaseKeyMask],
  val sameScreen: Boolean
) extends Event(3)

case class ButtonPress (
  val button: Button,
  val sequenceNumber: Card16,
  val time: Timestamp,
  val root: Window,
  val event: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val eventX: Int16,
  val eventY: Int16,
  val state: Set[BaseKeyMask],
  val sameScreen: Boolean
) extends Event(4)

case class ButtonRelease (
  val button: Button,
  val sequenceNumber: Card16,
  val time: Timestamp,
  val root: Window,
  val event: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val eventX: Int16,
  val eventY: Int16,
  val state: Set[BaseKeyMask],
  val sameScreen: Boolean
) extends Event(5)

case class MotionNotify (
  val detail: Card8,
  val sequenceNumber: Card16,
  val time: Timestamp,
  val root: Window,
  val event: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val eventX: Int16,
  val eventY: Int16,
  val state: Set[BaseKeyMask],
  val sameScreen: Boolean
) extends Event(6)

case class EnterNotify (
  val detail: Card8,
  val sequenceNumber: Card16,
  val time: Timestamp,
  val root: Window,
  val event: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val eventX: Int16,
  val eventY: Int16,
  val state: Set[BaseKeyMask],
  val sameScreen: Boolean,
  val focus: UInt8
) extends Event(7)

case class LeaveNotify (
  val detail: Card8,
  val sequenceNumber: Card16,
  val time: Timestamp,
  val root: Window,
  val event: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val eventX: Int16,
  val eventY: Int16,
  val state: Set[BaseKeyMask],
  val sameScreen: Boolean,
  val focus: UInt8
) extends Event(8)

case class FocusIn (
  val detail: Card8,
  val sequenceNumber: Card16,
  val event: Window,
  val mode: Card8
) extends Event(9)

case class FocusOut (
  val detail: Card8,
  val sequenceNumber: Card16,
  val event: Window,
  val mode: Card8
) extends Event(10)

object Focus {
  def apply(stream: BinaryInputStream, code: UInt8) = {
    val detail = stream.readUInt8()
    val sequenceNumber = stream.readUInt16()
    val event = stream.readUInt32()
    val mode = stream.readUInt8()
    stream.skip(23)
    code.value match {
      case 9 => new FocusIn(detail, sequenceNumber, event, mode)
      case 10 => new FocusOut(detail, sequenceNumber, event, mode)
    }
  }
}

case class KeymapNotify (
  val keys: List[Keycode]
) extends Event(11)

object KeymapNotify {
  def apply(stream: BinaryInputStream) = {
    var keys = new Array[Byte](31)
    stream.read(keys, 0, 31)
    new KeymapNotify(keys map { i => UInt8(i.toInt) } toList)
  }
}

case class Expose (
  val sequenceNumber: Card16,
  val window: Window,
  val x: Card16,
  val y: Card16,
  val width: Card16,
  val height: Card16,
  val count: Card16
) extends Event(12)

object Expose {
  def apply(stream: BinaryInputStream) = {
    val sequenceNumber = stream.readUInt16()
    val window = stream.readUInt32()
    val x = stream.readUInt16()
    val y = stream.readUInt16()
    val width = stream.readUInt16()
    val height = stream.readUInt16()
    val count = stream.readUInt16()
    stream.skip(14)
    new Expose(sequenceNumber, window, x, y, width, height, count)
  }
}

case class GraphicsExposure (
  val sequenceNumber: Card16,
  val window: Window,
  val x: Card16,
  val y: Card16,
  val width: Card16,
  val height: Card16,
  val minorOpCode: Card16,
  val count: Card16,
  val majorOpCode: Card8
) extends Event(13)

object GraphicsExposure {
  def apply(stream: BinaryInputStream) = {
    val sequenceNumber = stream.readUInt16()
    val window = stream.readUInt32()
    val x = stream.readUInt16()
    val y = stream.readUInt16()
    val width = stream.readUInt16()
    val height = stream.readUInt16()
    val minorOpCode = stream.readUInt16()
    val count = stream.readUInt16()
    val majorOpCode = stream.readUInt8()
    stream.skip(11)
    new GraphicsExposure(sequenceNumber, window, x, y, width, height, minorOpCode, count, majorOpCode)
  }
}

case class NoExposure (
  val sequenceNumber: Card16,
  val drawable: Drawable,
  val minorOpCode: Card16,
  val majorOpCode: Card8
) extends Event(14)

object NoExposure {
  def apply(stream: BinaryInputStream) = {
    val sequenceNumber = stream.readUInt16()
    val drawable = stream.readUInt32()
    val minorOpCode = stream.readUInt16()
    val majorOpCode = stream.readUInt8()
    stream.skip(21)
    new NoExposure(sequenceNumber, drawable, minorOpCode, majorOpCode)
  }
}

case class VisibilityNotify (
  val sequenceNumber: Card16,
  val window: Window,
  val state: Card8
) extends Event(15)

object VisibilityNotify {
  def apply(stream: BinaryInputStream) = {
    val sequenceNumber = stream.readUInt16()
    val window = stream.readUInt32()
    val state = stream.readUInt8()
    stream.skip(23)
    new VisibilityNotify(sequenceNumber, window, state)
  }
}

case class CreateNotify (
  val sequenceNumber: Card16,
  val parent: Window,
  val window: Window,
  val x: Int16,
  val y: Int16,
  val width: Card16,
  val height: Int16,
  val borderWidth: Card16,
  val overrideReidrect: Boolean
) extends Event(16)

object CreateNotify {
  def apply(stream: BinaryInputStream) = {
    val sequenceNumber = stream.readUInt16()
    val parent = stream.readUInt32()
    val window = stream.readUInt32()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val width = stream.readUInt16()
    val height = stream.readInt16()
    val borderWidth = stream.readUInt16()
    val overrideRedirect = stream.readBoolean()
    stream.skip(9)
    new CreateNotify(sequenceNumber, parent, window, x, y, width, height, borderWidth, overrideRedirect)
  }
}

case class DestroyNotify(
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window
) extends Event(17)

object DestroyNotify {
  def apply(stream: BinaryInputStream) = {
    val sequenceNumber = stream.readUInt16()
    val event = stream.readUInt32()
    val window = stream.readUInt32()
    stream.skip(20)
    new DestroyNotify(sequenceNumber, event, window)
  }
}

case class UnmapNotify (
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window,
  val fromConfigure: Boolean
) extends Event(17)

object UnmapNotify {
  def apply(stream: BinaryInputStream) = {
    val sequenceNumber = stream.readUInt16()
    val event = stream.readUInt32()
    val window = stream.readUInt32()
    val fromConfigure = stream.readBoolean()
    stream.skip(19)
    new UnmapNotify(sequenceNumber, event, window, fromConfigure)
  }
}

object Event {
  def firstTypeFromStream(stream: BinaryInputStream) = {
    val code = stream.readUInt8()
    val detail = stream.readUInt8()
    val sequenceNumber = stream.readUInt16()
    val time = stream.readUInt32()
    val root = stream.readUInt32()
    val event = stream.readUInt32()
    val child = stream.readUInt32()
    val rootX = stream.readInt16()
    val rootY = stream.readInt16()
    val eventX = stream.readInt16()
    val eventY = stream.readInt16()
    val state = KeyButMask.fromMask(stream.readInt16())
    val sameScreen = stream.readBoolean()
    val rest = stream.readUInt8()
    code.value match {
      case 2 => new KeyPress(detail, sequenceNumber, time, root, event,
        child, rootX, rootY, eventX, eventY, state, sameScreen)
      case 3 => new KeyRelease(detail, sequenceNumber, time, root, event,
        child, rootX, rootY, eventX, eventY, state, sameScreen)
      case 4 => new ButtonPress(detail, sequenceNumber, time, root, event,
        child, rootX, rootY, eventX, eventY, state, sameScreen)
      case 5 => new ButtonRelease(detail, sequenceNumber, time, root, event,
        child, rootX, rootY, eventX, eventY, state, sameScreen)
      case 6 => new MotionNotify(detail, sequenceNumber, time, root, event,
        child, rootX, rootY, eventX, eventY, state, sameScreen)
      case 7 => new EnterNotify(detail, sequenceNumber, time, root, event,
        child, rootX, rootY, eventX, eventY, state, sameScreen, rest)
      case 8 => new LeaveNotify(detail, sequenceNumber, time, root, event,
        child, rootX, rootY, eventX, eventY, state, sameScreen, rest)
    }
  }
}
