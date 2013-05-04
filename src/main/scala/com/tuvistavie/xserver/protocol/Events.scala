package com.tuvistavie.xserver.protocol.events

import com.tuvistavie.xserver.protocol.types._
import com.tuvistavie.xserver.protocol.types.atoms.BaseAtom

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
    stream.skip(1)
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
    stream.skip(1)
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
    stream.skip(1)
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
    stream.skip(1)
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
  val overrideRedirect: Boolean
) extends Event(16)

object CreateNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
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
    stream.skip(1)
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
) extends Event(18)

object UnmapNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readUInt16()
    val event = stream.readUInt32()
    val window = stream.readUInt32()
    val fromConfigure = stream.readBoolean()
    stream.skip(19)
    new UnmapNotify(sequenceNumber, event, window, fromConfigure)
  }
}

case class MapNotify (
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window,
  val overrideRedirect: Boolean
) extends Event(19)

object MapNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readUInt16()
    val event = stream.readUInt32()
    val window = stream.readUInt32()
    val fromConfigure = stream.readBoolean()
    stream.skip(19)
    new MapNotify(sequenceNumber, event, window, fromConfigure)
  }
}

case class MapRequest (
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window
) extends Event(20)

object MapRequest {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readUInt16()
    val event = stream.readUInt32()
    val window = stream.readUInt32()
    stream.skip(20)
    new MapRequest(sequenceNumber, event, window)
  }
}

case class ReparentNotify (
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window,
  val parent: Window,
  val x: Int16,
  val y: Int16,
  val overrideRedirect: Boolean
) extends Event(21)

object ReparentNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val event = stream.readWindow()
    val window = stream.readWindow()
    val parent = stream.readWindow()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val overrideRedirect = stream.readBoolean()
    stream.skip(11)
    new ReparentNotify(sequenceNumber, event, window, parent, x, y, overrideRedirect)
  }
}

case class ConfigureNotify (
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window,
  val parent: Window,
  val x: Int16,
  val y: Int16,
  val width: Card16,
  val height: Card16,
  val borderWidth: Card16,
  val overrideRedirect: Boolean
) extends Event(22)

object ConfigureNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val event = stream.readWindow()
    val window = stream.readWindow()
    val parent = stream.readWindow()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val width = stream.readCard16()
    val height = stream.readCard16()
    val borderWidth = stream.readCard16()
    val overrideRedirect = stream.readBoolean()
    stream.skip(5)
    new ConfigureNotify(sequenceNumber, event, window, parent, x, y, width, height, borderWidth, overrideRedirect)
  }
}

case class ConfigureRequest (
  val stackMode: Card8,
  val sequenceNumber: Card16,
  val parent: Window,
  val window: Window,
  val sibling: Window,
  val x: Int16,
  val y: Int16,
  val width: Card16,
  val height: Card16,
  val borderWidth: Card16,
  val mask: Bitmask
) extends Event(23)

object ConfigureRequest {
  def apply(stream: BinaryInputStream) = {
    val stackMode = stream.readCard8()
    val sequenceNumber = stream.readCard16()
    val parent = stream.readWindow()
    val window = stream.readWindow()
    val sibling = stream.readWindow()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val width = stream.readCard16()
    val height = stream.readCard16()
    val borderWidth = stream.readCard16()
    val mask = stream.readBitmask()
    stream.skip(4)
    new ConfigureRequest(stackMode, sequenceNumber, parent, window,
      sibling, x, y, width, height, borderWidth, mask)
  }
}

case class GravityNotify (
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window,
  val x: Int16,
  val y: Int16
) extends Event(24)

object GravityNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val event = stream.readWindow()
    val window = stream.readWindow()
    val x = stream.readInt16()
    val y = stream.readInt16()
    stream.skip(16)
    new GravityNotify(sequenceNumber, event, window, x, y)
  }
}

case class ResizeRequest (
  val sequenceNumber: Card16,
  val window: Window,
  val width: Card16,
  val height: Card16
) extends Event(25)

object ResizeRequest {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val window = stream.readWindow()
    val width = stream.readCard16()
    val height = stream.readCard16()
    stream.skip(20)
    new ResizeRequest(sequenceNumber, window, width, height)
  }
}

case class CirculateNotify (
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window,
  val unused: Window,
  val place: Card8
) extends Event(26)

object CirculateNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val event = stream.readWindow()
    val window = stream.readWindow()
    val unused = stream.readWindow()
    val place = stream.readCard8()
    stream.skip(15)
    new CirculateNotify(sequenceNumber, event, window, unused, place)
  }
}

case class CirculateRequest (
  val sequenceNumber: Card16,
  val event: Window,
  val window: Window,
  val unused: Window,
  val place: Card8
) extends Event(27)

object CirculateRequest {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val event = stream.readWindow()
    val window = stream.readWindow()
    val unused = stream.readWindow()
    val place = stream.readCard8()
    stream.skip(15)
    new CirculateRequest(sequenceNumber, event, window, unused, place)
  }
}

case class PropertyNotify (
  val sequenceNumber: Card16,
  val window: Window,
  val atom: BaseAtom,
  val time: Timestamp,
  val state: Card8
) extends Event(28)

object PropertyNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val window = stream.readWindow()
    val atom = stream.readUInt32().toAtom
    val time = stream.readTimestamp()
    val state = stream.readCard8()
    stream.skip(15)
    new PropertyNotify(sequenceNumber, window, atom, time, state)
  }
}

case class SelectionClear (
  val sequenceNumber: Card16,
  val time: Timestamp,
  val owner: Window,
  val selection: BaseAtom
) extends Event(29)

object SelectionClear {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val time = stream.readTimestamp()
    val owner = stream.readWindow()
    val selection = stream.readUInt32().toAtom
    stream.skip(16)
    new SelectionClear(sequenceNumber, time, owner, selection)
  }
}

case class SelectionRequest (
  val sequenceNumber: Card16,
  val time: Timestamp,
  val owner: Window,
  val requestor: Window,
  val selection: BaseAtom,
  val target: BaseAtom,
  val property: BaseAtom
) extends Event(30)

object SelectionRequest {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val time = stream.readTimestamp()
    val owner = stream.readWindow()
    val requestor = stream.readWindow()
    val selection = stream.readUInt32().toAtom
    val target = stream.readUInt32().toAtom
    val property = stream.readUInt32().toAtom
    stream.skip(4)
    new SelectionRequest(sequenceNumber, time, owner, requestor, selection, target, property)
  }
}

case class SelectionNotify (
  val sequenceNumber: Card16,
  val time: Timestamp,
  val requestor: Window,
  val selection: BaseAtom,
  val target: BaseAtom,
  val property: BaseAtom
) extends Event(31)

object SelectionNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val time = stream.readTimestamp()
    val requestor = stream.readWindow()
    val selection = stream.readUInt32().toAtom
    val target = stream.readUInt32().toAtom
    val property = stream.readUInt32().toAtom
    stream.skip(8)
    new SelectionNotify(sequenceNumber, time, requestor, selection, target, property)
  }
}

case class ColormapNotify(
  val sequenceNumber: Card16,
  val window: Window,
  val colormap: Colormap,
  val isNew: Boolean,
  val state: UInt8
) extends Event(32)

object ColormapNotify {
  def apply(stream: BinaryInputStream) = {
    stream.skip(1)
    val sequenceNumber = stream.readCard16()
    val window = stream.readWindow()
    val colormap = stream.readColormap()
    val isNew = stream.readBoolean()
    val state = stream.readUInt8()
    stream.skip(18)
    new ColormapNotify(sequenceNumber, window, colormap, isNew, state)
  }
}

case class ClientMessage (
  val format: Card8,
  val sequenceNumber: Card16,
  val window: Window,
  val messageType: BaseAtom,
  val data: Array[Byte]
) extends Event(33)

object ClientMessage {
  def apply(stream: BinaryInputStream) = {
    val format = stream.readCard8()
    val sequenceNumber = stream.readCard16()
    val window = stream.readWindow()
    val messageType = stream.readUInt32().toAtom
    var data = new Array[Byte](20)
    stream.read(data, 0, 20)
    new ClientMessage(format, sequenceNumber, window, messageType, data)
  }
}

case class MappingNotify (
  val sequenceNumber: Card16,
  val request: Card8,
  val firstKeycode: Keycode,
  val count: Card8
) extends Event(34)

object MappingNotify {
  def apply(stream: BinaryInputStream) = {
    val sequenceNumber = stream.readCard16()
    val request = stream.readCard8()
    val firstKeycode = stream.readKeycode()
    val count = stream.readCard8()
    new MappingNotify(sequenceNumber, request, firstKeycode, count)
  }
}


object Event {

  def apply(stream: BinaryInputStream) = {
    val code = stream.readUInt8()
    code.value match {
      case n if n <= 8 => createBasicEvent(stream, n)
      case 9 | 10  => Focus(stream, code.value)
      case 11 => KeymapNotify(stream)
      case 12 => Expose(stream)
      case 13 => GraphicsExposure(stream)
      case 14 => NoExposure(stream)
      case 15 => VisibilityNotify(stream)
      case 16 => CreateNotify(stream)
      case 17 => DestroyNotify(stream)
      case 18 => UnmapNotify(stream)
      case 19 => MapNotify(stream)
      case 20 => MapRequest(stream)
      case 21 => ReparentNotify(stream)
      case 22 => ConfigureNotify(stream)
      case 23 => ConfigureRequest(stream)
      case 24 => GravityNotify(stream)
      case 25 => ResizeRequest(stream)
      case 26 => CirculateNotify(stream)
      case 27 => CirculateRequest(stream)
      case 28 => PropertyNotify(stream)
      case 29 => SelectionClear(stream)
      case 30 => SelectionRequest(stream)
      case 31 => SelectionNotify(stream)
      case 32 => ColormapNotify(stream)
      case 33 => ClientMessage(stream)
      case 34 => MappingNotify(stream)
    }
  }

  def createBasicEvent(stream: BinaryInputStream, code: Int) = {
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
    code match {
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
