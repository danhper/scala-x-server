package com.tuvistavie.xserver.protocol.requests

import scala.collection.mutable
import com.tuvistavie.util.IntTimes._
import com.tuvistavie.xserver.io._
import com.tuvistavie.xserver.protocol.types._
import com.tuvistavie.xserver.protocol.events.Event
import com.tuvistavie.xserver.protocol.types.atoms.Atom

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

object WindowValue {
  def apply(stream: BinaryInputStream, mask: Int): Map[String, Value] = {
    val values = mutable.Map[String, Value]()
    if((mask & 0x0001) != 0) values += ("backgroundPixmap" -> stream.readPixmap())
    if((mask & 0x0002) != 0) values += ("backgroundPixel" -> stream.readCard32())
    if((mask & 0x0004) != 0) values += ("borderPixmap" -> stream.readPixmap())
    if((mask & 0x0008) != 0) values += ("borderPixel" -> stream.readCard32())
    if((mask & 0x0010) != 0) values += ("bitGravity" -> stream.readBitGravity(4))
    if((mask & 0x0020) != 0) values += ("windowGravity" -> stream.readWindowGravity(4))
    if((mask & 0x0040) != 0) values += ("backingStore" -> stream.readCard8(4))
    if((mask & 0x0080) != 0) values += ("backingPlanes" -> stream.readCard32())
    if((mask & 0x0100) != 0) values += ("backingPixels" -> stream.readCard32())
    if((mask & 0x0200) != 0) values += ("overrideRedirect" -> stream.readBool(4))
    if((mask & 0x0400) != 0) values += ("saveUnder" -> stream.readBool(4))
    if((mask & 0x0800) != 0) values += ("eventMask" -> stream.readSetOfEvent())
    if((mask & 0x1000) != 0) values += ("noPropagateMask" -> stream.readSetOfDeviceEvent())
    if((mask & 0x2000) != 0) values += ("colorMap" -> stream.readColormap())
    if((mask & 0x4000) != 0) values += ("cursor" -> stream.readCursor())
    Map(values.toStream: _*)
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
  val values: Map[String, Value]
  ) extends Request(1)

object CreateWindow {
  def apply(stream: BinaryInputStream, depth: Card8) = {
    val windowId = stream.readWindow()
    val parent = stream.readWindow()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val width = stream.readCard16()
    val height = stream.readCard16()
    val borderWidth = stream.readCard16()
    val windowClass = stream.readUInt16()
    val visualId = stream.readVisualID()
    val bitmask = stream.readUInt32()
    val values =  WindowValue(stream, bitmask)
    new CreateWindow(depth, windowId, parent, x, y, width,
      height, borderWidth, windowClass, visualId, values)
  }
}

case class ChangeWindowAttributes (
  val window: Window,
  val values: Map[String, Value]
  ) extends Request(2)

object ChangeWindowAttributes {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
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
    new GetWindowAttributes(stream.readWindow())
  }
}

case class DestroyWindow (
  val window: Window
  ) extends Request(4)

object DestroyWindow {
  def apply(stream: BinaryInputStream) = {
    new DestroyWindow(stream.readWindow())
  }
}

case class DestroySubWindows (
  val window: Window
  ) extends Request(5)

object DestroySubWindows {
  def apply(stream: BinaryInputStream) = {
    new DestroySubWindows(stream.readWindow())
  }
}

case class ChangeSaveSet (
  val mode: UInt8,
  val window: Window
  ) extends Request(6)

object ChangeSaveSet {
  def apply(stream: BinaryInputStream, mode: UInt8) = {
    new ChangeSaveSet(mode, stream.readWindow())
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
    val window = stream.readWindow()
    val parent = stream.readWindow()
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
    new MapWindow(stream.readWindow())
  }
}

case class MapSubwindows (
  val window: Window
  ) extends Request(9)

object MapSubwindows {
  def apply(stream: BinaryInputStream) = {
    new MapSubwindows(stream.readWindow())
  }
}

case class UnmapWindow (
  val window: Window
  ) extends Request(10)

object UnmapWindow {
  def apply(stream: BinaryInputStream) = {
    new UnmapWindow(stream.readWindow())
  }
}

case class UnmapSubwindows (
  val window: Window
  ) extends Request(11)

object UnmapSubwindows {
  def apply(stream: BinaryInputStream) = {
    new UnmapSubwindows(stream.readWindow())
  }
}

object ConfigureWindowValue {
  def apply(stream: BinaryInputStream, mask: Int): Map[String, Value] = {
    val values = mutable.Map[String, Value]()
    if((mask & 0x0001) != 0) values += ("x" -> stream.readInt16(4))
    if((mask & 0x0002) != 0) values += ("y" -> stream.readInt16(4))
    if((mask & 0x0004) != 0) values += ("width" -> stream.readCard16(4))
    if((mask & 0x0008) != 0) values += ("height" -> stream.readCard16(4))
    if((mask & 0x0010) != 0) values += ("borderWidth" -> stream.readCard16(4))
    if((mask & 0x0020) != 0) values += ("sibling" -> stream.readWindow())
    if((mask & 0x0040) != 0) values += ("stackMode" -> stream.readCard8(4))
    Map(values.toStream: _*)
  }
}

case class ConfigureWindow (
  val window: Window,
  val values: Map[String, Value]
  ) extends Request(12)

object ConfigureWindow {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
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
    new CirculateWindow(direction, stream.readWindow())
  }
}

case class GetGeometry (
  val drawable: Drawable
  ) extends Request(14)

object GetGeometry {
  def apply(stream: BinaryInputStream) = {
    new GetGeometry(stream.readDrawable())
  }
}

case class QueryTree (
  val window: Window
  ) extends Request(15)

object QueryTree {
  def apply(stream: BinaryInputStream) = {
    new QueryTree(stream.readWindow())
  }
}

case class InternAtom (
  val onlyIfExists: Bool,
  val name: Str
  ) extends Request(16)

object InternAtom {
  def apply(stream: BinaryInputStream, onlyIfExists: Card8) = {
    val n = stream.readUInt16()
    stream.skip(2)
    val name = stream.readString8(n)
    stream.readPad(n)
    new InternAtom(onlyIfExists.toBool, name)
  }
}

case class GetAtomName (
  val atom: Atom
  ) extends Request(17)

object GetAtomName {
  def apply(stream: BinaryInputStream) = {
    val atom = stream.readAtom()
    new GetAtomName(atom)
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
    val window = stream.readWindow()
    val property = stream.readAtom()
    val propertyType = stream.readAtom()
    val format = stream.readUInt8()
    stream.skip(3)
    val n = stream.readUInt32() * (format / 8)
    val data = mutable.MutableList[UInt8]()
    n times { data += stream.readUInt8() }
    stream.readPad(UInt32(n))
    new ChangeProperty(mode, window, property, propertyType, format, data.toList)
  }
}

case class DeleteProperty (
  val window: Window,
  val property: Atom
  ) extends Request(19)

object DeleteProperty {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    val property = stream.readAtom()
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
  def apply(stream: BinaryInputStream, delete: Card8) = {
    val window = stream.readWindow()
    val property = stream.readAtom()
    val propertyType = stream.readAtom()
    val longOffset = stream.readUInt32()
    val longLength = stream.readUInt32()
    new GetProperty(delete.toBool, window, property, propertyType, longOffset, longLength)
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
    val owner = stream.readWindow()
    val selection = stream.readAtom()
    val time = stream.readUInt32()
    new SetSelectionOwner(owner, selection, time)
  }
}


case class GetSelectionOwner (
  val selection: Atom
  ) extends Request(23)

object GetSelectionOwner {
  def apply(stream: BinaryInputStream) = {
    new GetSelectionOwner(stream.readAtom())
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
    val selection = stream.readAtom()
    val target = stream.readAtom()
    val property = stream.readAtom()
    val time = stream.readUInt32()
    new ConvertSelection(window, selection, target, property, time)
  }
}

case class SendEvent (
  val propagate: Bool,
  val window: Window,
  val eventMask: SetOfEvent,
  val event: Event
) extends Request(25)

object SendEvent {
  def apply(stream: BinaryInputStream, propagate: UInt8) = {
    val window = stream.readWindow()
    val eventMask = stream.readSetOfEvent()
    val event = Event(stream)
    new SendEvent(propagate.toBool, window, eventMask, event)
  }
}

case class GrabPointer (
  val ownerEvents: Bool,
  val grabWindow: Window,
  val eventMask: SetOfEvent,
  val pointerMode: Card8,
  val keyboardMode: Card8,
  var confineTo: Window,
  val cursor: Cursor,
  val time: Timestamp
) extends Request(26)

object GrabPointer {
  def apply(stream: BinaryInputStream, ownerEvents: Card8) = {
    val grabWindow = stream.readWindow()
    val eventMask = stream.readSetOfEvent()
    val pointerMode = stream.readCard8()
    val keyboardMode = stream.readCard8()
    var confineTo = stream.readWindow()
    val cursor = stream.readCursor()
    val time = stream.readTimestamp()
    new GrabPointer(ownerEvents.toBool, grabWindow, eventMask, pointerMode,
      keyboardMode, confineTo, cursor, time)
  }
}

case class UngrabPointer (
  val time: Timestamp
) extends Request(27)

object UngrabPointer {
  def apply(stream: BinaryInputStream) = {
    val time = stream.readTimestamp()
    new UngrabPointer(time)
  }
}

case class GrabButton (
  val ownerEvents: Bool,
  val grabWindow: Window,
  val eventMask: SetOfPointerEvent,
  val pointerMode: Card8,
  val keyboardMode: Card8,
  val confineTo: Window,
  val cursor: Cursor,
  val button: Button,
  val modifiers: SetOfKeyMask
) extends Request(28)

object GrabButton {
  def apply(stream: BinaryInputStream, ownerEvents: Card8) = {
    val grabWindow = stream.readWindow()
    val eventMask = stream.readSetOfPointerEvent()
    val pointerMode = stream.readCard8()
    val keyboardMode = stream.readCard8()
    val confineTo = stream.readWindow()
    val cursor = stream.readCursor()
    val button = stream.readButton()
    stream.skip(1)
    val modifiers = stream.readSetOfKeyMask()
    new GrabButton(ownerEvents.toBool, grabWindow, eventMask, pointerMode,
      keyboardMode, confineTo, cursor, button, modifiers)
  }
}

case class UngrabButton (
  val button: Button,
  val grabWindow: Window,
  val modifiers: SetOfKeyMask
) extends Request(29)

object UngrabButton {
  def apply(stream: BinaryInputStream, button: Button) = {
    val grabWindow = stream.readWindow()
    val modifiers = stream.readSetOfKeyMask()
    stream.skip(2)
    new UngrabButton(button, grabWindow, modifiers)
  }
}

case class ChangeActivePointerGrab (
  val cursor: Cursor,
  val time: Timestamp,
  val eventMask: SetOfPointerEvent
) extends Request(30)

object ChangeActivePointerGrab {
  def apply(stream: BinaryInputStream) = {
    val cursor = stream.readCursor()
    val time = stream.readTimestamp()
    val eventMask = stream.readSetOfPointerEvent()
  }
}

case class GrabKeyboard (
  val ownerEvents: Bool,
  val grabWindow: Window,
  val time: Timestamp,
  val pointerMode: Card8,
  val keyboardMode: Card8
) extends Request(31)

object GrabKeyboard {
  def apply(stream: BinaryInputStream, ownerEvents: Card8) = {
    val grabWindow = stream.readWindow()
    val time = stream.readTimestamp()
    val pointerMode = stream.readCard8()
    val keyboardMode = stream.readCard8()
    stream.skip(2)
    new GrabKeyboard(ownerEvents.toBool, grabWindow, time, pointerMode, keyboardMode)
  }
}

case class UngrabKeyboard (
  val time: Timestamp
) extends Request(32)

object UngrabKeyboard {
  def apply(stream: BinaryInputStream) = {
    val time = stream.readTimestamp()
    new UngrabKeyboard(time)
  }
}

case class GrabKey (
  val ownerEvents: Bool,
  val grabWindow: Window,
  val modifiers: SetOfKeyMask,
  val key: Keycode,
  val pointerMode: Card8,
  val keyboardMode: Card8
) extends Request(33)

object GrabKey {
  def apply(stream: BinaryInputStream, ownerEvents: Card8) = {
    val grabWindow = stream.readWindow()
    val modifiers = stream.readSetOfKeyMask()
    val key = stream.readKeycode()
    val pointerMode = stream.readCard8()
    val keyboardMode = stream.readCard8()
    stream.skip(2)
    new GrabKey(ownerEvents.toBool, grabWindow,
      modifiers, key, pointerMode, keyboardMode)
  }
}

case class UngrabKey (
  val key: Keycode,
  val grabWindow: Window,
  val modifiers: SetOfKeyMask
) extends Request(34)

object UngrabKey {
  def apply(stream: BinaryInputStream, key: Keycode) = {
    val window = stream.readWindow()
    val modifiers = stream.readSetOfKeyMask()
    stream.skip(2)
    new UngrabKey(key, window, modifiers)
  }
}

case class AllowEvents (
  val mode: Card8,
  val time: Timestamp
) extends Request(35)

object AllowEvents {
  def apply(stream: BinaryInputStream, mode: Card8) = {
    val time = stream.readTimestamp()
    new AllowEvents(mode, time)
  }
}

case object GrabServer extends Request(36)

case object UngrabServer extends Request(37)

case class QueryPointer (
  val window: Window
) extends Request(38)

case object QueryPointer {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    new QueryPointer(window)
  }
}

case class GetMotionEvents (
  val window: Window,
  val start: Timestamp,
  val stop: Timestamp
) extends Request(39)

object GetMotionEvents {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    val start = stream.readTimestamp()
    val stop = stream.readTimestamp()
    new GetMotionEvents(window, start, stop)
  }
}

case class TranslateCoordinates (
  val srcWindow: Window,
  val dstWindow: Window,
  val srcX: Int16,
  val srcY: Int16
) extends Request(40)

object TranslateCoordinates {
  def apply(stream: BinaryInputStream) = {
    val srcWindow = stream.readWindow()
    val dstWindow = stream.readWindow()
    val srcX = stream.readInt16()
    val srcY = stream.readInt16()
    new TranslateCoordinates(srcWindow, dstWindow, srcX, srcY)
  }
}

case class WarpPointer (
  val srcWindow: Window,
  val dstWindow: Window,
  val srcX: Int16,
  val srcY: Int16,
  val srcWidth: Card16,
  val srcHeight: Card16,
  val dstX: Int16,
  val dstY: Int16
) extends Request(41)

object WarpPointer {
  def apply(stream: BinaryInputStream) = {
    val srcWindow = stream.readWindow()
    val dstWindow = stream.readWindow()
    val srcX = stream.readInt16()
    val srcY = stream.readInt16()
    val srcWidth = stream.readCard16()
    val srcHeight = stream.readCard16()
    val dstX = stream.readInt16()
    val dstY = stream.readInt16()
    new WarpPointer(srcWindow, dstWindow, srcX, srcY,
      srcWidth, srcHeight, dstX, dstY)
  }
}

case class SetInputFocus (
  val revertTo: Card8,
  val focus: Window,
  val time: Timestamp
) extends Request(42)

object SetInputFocus {
  def apply(stream: BinaryInputStream, revertTo: Card8) = {
    val focus = stream.readWindow()
    val time = stream.readTimestamp()
    new SetInputFocus(revertTo, focus, time)
  }
}

case object GetInputFocus extends Request(43)

case object QueryKeymap extends Request(44)

case class OpenFont (
  val font: Font,
  val name: Str
) extends Request(45)

object OpenFont {
  def apply(stream: BinaryInputStream) = {
    val font = stream.readFont()
    val n = stream.readCard16()
    stream.skip(2)
    val name = stream.readString8(n)
    stream.readPad(n)
    new OpenFont(font, name)
  }
}

case class CloseFont (
  val font: Font
) extends Request(46)

object CloseFont {
  def apply(stream: BinaryInputStream) = {
    val font = stream.readFont()
    new CloseFont(font)
  }
}

case class QueryFont (
  val font: Fontable
) extends Request(47)

object QueryFont {
  def apply(stream: BinaryInputStream) = {
    val font = stream.readFontable()
    new QueryFont(font)
  }
}

case class QueryTextExtents (
  val font: Fontable,
  val string: Str
) extends Request(48)

object QueryTextExtents {
  def apply(stream: BinaryInputStream, oddLength: Bool, requestLength: Card16) = {
    val font = stream.readFontable()
    val stringAndPaddingLength = (requestLength - 2) * 4
    val n = if(oddLength.value) stringAndPaddingLength - 2
            else stringAndPaddingLength
    stream.readString16(n / 2)
    if(oddLength.value) stream.skip(2)
  }
}

case class ListFonts (
  val maxNames: Card16,
  val pattern: Str
) extends Request(49)

object ListFonts {
  def apply(stream: BinaryInputStream) = {
    val maxNames = stream.readCard16()
    val n = stream.readCard16()
    val pattern = stream.readString8(n)
    stream.readPad(n)
    new ListFonts(maxNames, pattern)
  }
}

case class ListFontsWithInfo (
  val maxNames: Card16,
  val pattern: Str
) extends Request(50)

object ListFontsWithInfo {
  def apply(stream: BinaryInputStream) = {
    val maxNames = stream.readCard16()
    val n = stream.readCard16()
    val pattern = stream.readString8(n)
    stream.readPad(n)
    new ListFontsWithInfo(maxNames, pattern)
  }
}

case class SetFontPath (
  val strings: List[Str]
) extends Request(51)

object SetFontPath {
  def apply(stream: BinaryInputStream) = {
    val n = stream.readCard16()
    stream.skip(2)
    val strings = stream.readListOfStr(n)
    new SetFontPath(strings)
  }
}

case class GetFontPath (
  val requestList: Card16
) extends Request(52)

object GetFontPath {
  def apply(stream: BinaryInputStream) = {
    val requestList = stream.readCard16()
    new GetFontPath(requestList)
  }
}

case class CreatePixmap (
  val depth: Card8,
  val pid: Pixmap,
  val drawable: Drawable,
  val width: Card16,
  val height: Card16
) extends Request(53)

object CreatePixmap {
  def apply(stream: BinaryInputStream, depth: Card8) = {
    val pid = stream.readPixmap()
    val drawable = stream.readDrawable()
    val width = stream.readCard16()
    val height = stream.readCard16()
    new CreatePixmap(depth, pid, drawable, width, height)
  }
}

case class FreePixmap (
  val pixmap: Pixmap
) extends Request(54)

object FreePixmap {
  def apply(stream: BinaryInputStream) = {
    val pixmap = stream.readPixmap()
    new FreePixmap(pixmap)
  }
}

object GCValue {
  def apply(stream: BinaryInputStream, mask: Int): Map[String, Value] = {
    val values = mutable.Map[String, Value]()
    if((mask & 0x00000001) != 0) values += ("function" -> stream.readCard8(4))
    if((mask & 0x00000002) != 0) values += ("planeMask" -> stream.readCard32())
    if((mask & 0x00000004) != 0) values += ("foreground" -> stream.readCard32())
    if((mask & 0x00000008) != 0) values += ("background" -> stream.readCard32())
    if((mask & 0x00000010) != 0) values += ("lineWidth" -> stream.readCard16(4))
    if((mask & 0x00000020) != 0) values += ("lineStyle" -> stream.readCard8(4))
    if((mask & 0x00000040) != 0) values += ("capStyle" -> stream.readCard8(4))
    if((mask & 0x00000080) != 0) values += ("joinStyle" -> stream.readCard8(4))
    if((mask & 0x00000100) != 0) values += ("fillStyle" -> stream.readCard8(4))
    if((mask & 0x00000200) != 0) values += ("fillRule" -> stream.readCard8(4))
    if((mask & 0x00000400) != 0) values += ("tile" -> stream.readPixmap())
    if((mask & 0x00000800) != 0) values += ("stipple" -> stream.readPixmap())
    if((mask & 0x00001000) != 0) values += ("tileStippleXOrigin" -> stream.readInt16(2))
    if((mask & 0x00002000) != 0) values += ("tileStippleYOrigin" -> stream.readInt16(2))
    if((mask & 0x00004000) != 0) values += ("font" -> stream.readFont())
    if((mask & 0x00008000) != 0) values += ("subwindowMode" -> stream.readCard8(4))
    if((mask & 0x00010000) != 0) values += ("graphicsExposures" -> stream.readBool(4))
    if((mask & 0x00020000) != 0) values += ("clipXOrigin" -> stream.readInt16(4))
    if((mask & 0x00040000) != 0) values += ("clipYOrigin" -> stream.readInt16(4))
    if((mask & 0x00080000) != 0) values += ("clipMask" -> stream.readPixmap())
    if((mask & 0x00100000) != 0) values += ("dashOffset" -> stream.readCard16(4))
    if((mask & 0x00200000) != 0) values += ("dashes" -> stream.readCard8(4))
    if((mask & 0x00400000) != 0) values += ("arcMode" -> stream.readCard8(4))
    Map(values.toStream: _*)
  }
}

case class CreateGC (
  val cid: GContext,
  val drawable: Drawable,
  val values: Map[String, Value]
) extends Request(55)

object CreateGC {
  def apply(stream: BinaryInputStream) = {
    val cid = stream.readGContext()
    val drawable = stream.readDrawable()
    val bitmask = stream.readBitmask()
    val values = GCValue(stream, bitmask)
    new CreateGC(cid, drawable, values)
  }
}

case class ChangeGC (
  val gc: GContext,
  val values: Map[String, Value]
) extends Request(56)

object ChangeGC {
  def apply(stream: BinaryInputStream) = {
    val gc = stream.readGContext()
    val bitmask = stream.readBitmask()
    val values = GCValue(stream, bitmask)
    new ChangeGC(gc, values)
  }
}

case class CopyGC (
  val srcGc: GContext,
  val dstGc: GContext,
  val bitmask: Bitmask
) extends Request(57)

object CopyGC {
  def apply(stream: BinaryInputStream) = {
    val srcGc = stream.readGContext()
    val dstGc = stream.readGContext()
    val bitmask = stream.readBitmask()
    new CopyGC(srcGc, dstGc, bitmask)
  }
}

case class SetDashes(
  val gc: GContext,
  val dashOffset: Card16,
  val dashes: List[Card8]
) extends Request(58)

object SetDashes {
  def apply(stream: BinaryInputStream) = {
    val gc = stream.readGContext()
    val dashOffset = stream.readCard16()
    val n = stream.readCard16()
    val dashes = stream.readListOfCard8(n)
    new SetDashes(gc, dashOffset, dashes)
  }
}

case class SetClipRectangles (
  val ordering: Card8,
  val gc: GContext,
  val clipXOrigin: Int16,
  val clipYOrigin: Int16,
  val rectangles: List[Rectangle]
) extends Request(59)

object SetClipRectangles {
  def apply(stream: BinaryInputStream, ordering: Card8, requestLength: Card16) = {
    val gc = stream.readGContext()
    val clipXOrigin = stream.readInt16()
    val clipYOrigin = stream.readInt16()
    val n = (requestLength - 3) / 2
    val rectangles = stream.readListOfRectangles(n)
    new SetClipRectangles(ordering, gc, clipXOrigin, clipYOrigin, rectangles)
  }
}

case class FreeGC (
  val gc: GContext
) extends Request(60)

object FreeGC {
  def apply(stream: BinaryInputStream) = {
    val gc = stream.readGContext()
    new FreeGC(gc)
  }
}

case class ClearArea (
  val exposures: Bool,
  val window: Window,
  val x: Int16,
  val y: Int16,
  val width: Card16,
  val height: Card16
) extends Request(61)


object ClearArea {
  def apply(stream: BinaryInputStream, exposures: Card8) = {
    val window = stream.readWindow()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val width = stream.readCard16()
    val height = stream.readCard16()
    new ClearArea(exposures.toBool, window, x, y, width, height)
  }
}

case class CopyArea (
  val srcDrawable: Drawable,
  val dstDrawable: Drawable,
  val gc: GContext,
  val srcX: Int16,
  val srcY: Int16,
  val dstX: Int16,
  val dstY: Int16,
  val width: Card16,
  val height: Card16
) extends Request(62)

object CopyArea {
  def apply(stream: BinaryInputStream) = {
    val srcDrawable = stream.readDrawable()
    val dstDrawable = stream.readDrawable()
    val gc = stream.readGContext()
    val srcX = stream.readInt16()
    val srcY = stream.readInt16()
    val dstX = stream.readInt16()
    val dstY = stream.readInt16()
    val width = stream.readCard16()
    val height = stream.readCard16()
    new CopyArea(srcDrawable, dstDrawable, gc,
      srcX, srcY, dstX, dstY, width, height)
  }
}

case class CopyPlane (
  val srcDrawable: Drawable,
  val dstDrawable: Drawable,
  val gc: GContext,
  val srcX: Int16,
  val srcY: Int16,
  val dstX: Int16,
  val dstY: Int16,
  val width: Card16,
  val height: Card16,
  val bitPlane: Card32
) extends Request(63)

object CopyPlane {
  def apply(stream: BinaryInputStream) = {
    val srcDrawable = stream.readDrawable()
    val dstDrawable = stream.readDrawable()
    val gc = stream.readGContext()
    val srcX = stream.readInt16()
    val srcY = stream.readInt16()
    val dstX = stream.readInt16()
    val dstY = stream.readInt16()
    val width = stream.readCard16()
    val height = stream.readCard16()
    val bitPlane = stream.readCard32()
    new CopyPlane(srcDrawable, dstDrawable, gc,
      srcX, srcY, dstX, dstY, width, height, bitPlane)
  }
}

case class PolyPoint (
  val coordinateMode: Card8,
  val drawable: Drawable,
  val gc: GContext,
  val points: List[Point]
) extends Request(64)

object PolyPoint {
  def apply(stream: BinaryInputStream, coordinateMode: Card8, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val points = stream.readListOfPoints(n)
    new PolyPoint(coordinateMode, drawable, gc, points)
  }
}

case class PolyLine (
  val coordinateMode: Card8,
  val drawable: Drawable,
  val gc: GContext,
  val points: List[Point]
) extends Request(65)

object PolyLine {
  def apply(stream: BinaryInputStream, coordinateMode: Card8, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val points = stream.readListOfPoints(n)
    new PolyLine(coordinateMode, drawable, gc, points)
  }
}

case class PolySegment (
  val coordinateMode: Card8,
  val drawable: Drawable,
  val gc: GContext,
  val segments: List[Segment]
) extends Request(66)

object PolySegment {
  def apply(stream: BinaryInputStream, coordinateMode: Card8, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val points = stream.readListOfSegments(n)
    new PolySegment(coordinateMode, drawable, gc, points)
  }
}

case class PolyRectangle (
  val coordinateMode: Card8,
  val drawable: Drawable,
  val gc: GContext,
  val rectangles: List[Rectangle]
) extends Request(66)

object PolyRectangle {
  def apply(stream: BinaryInputStream, coordinateMode: Card8, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val points = stream.readListOfRectangles(n)
    new PolyRectangle(coordinateMode, drawable, gc, points)
  }
}
