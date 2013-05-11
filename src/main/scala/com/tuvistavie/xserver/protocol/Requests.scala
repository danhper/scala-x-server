package com.tuvistavie.xserver.protocol.requests

import scala.collection.mutable
import com.tuvistavie.xserver.io._
import com.tuvistavie.xserver.protocol.types._
import com.tuvistavie.xserver.protocol.events.Event
import com.tuvistavie.xserver.protocol.types.atoms.Atom

abstract class Request(val opCode: Card8)

object Request {
  def apply(opCode: Int8, stream: BinaryInputStream) = {
    val data = stream.readCard8()
    val length = stream.readCard16()
    opCode value match {
      case   1 => CreateWindow(stream, data)
      case   2 => ChangeWindowAttributes(stream)
      case   3 => GetWindowAttributes(stream)
      case   4 => DestroyWindow(stream)
      case   5 => DestroySubWindows(stream)
      case   6 => ChangeSaveSet(stream, data)
      case   7 => ReparentWindow(stream)
      case   8 => MapWindow(stream)
      case   9 => MapSubwindows(stream)
      case  10 => UnmapWindow(stream)
      case  11 => UnmapSubwindows(stream)
      case  12 => ConfigureWindow(stream)
      case  13 => CirculateWindow(stream, data)
      case  14 => GetGeometry(stream)
      case  15 => QueryTree(stream)
      case  16 => InternAtom(stream, data)
      case  17 => GetAtomName(stream)
      case  18 => ChangeProperty(stream, data)
      case  19 => DeleteProperty(stream)
      case  20 => GetProperty(stream, data)
      case  21 => ListProperties(stream)
      case  22 => SetSelectionOwner(stream)
      case  23 => GetSelectionOwner(stream)
      case  24 => ConvertSelection(stream)
      case  25 => SendEvent(stream, data)
      case  26 => GrabPointer(stream, data)
      case  27 => UngrabPointer(stream)
      case  28 => GrabButton(stream, data)
      case  29 => UngrabButton(stream, data)
      case  30 => ChangeActivePointerGrab(stream)
      case  31 => GrabKeyboard(stream, data)
      case  32 => UngrabKeyboard(stream)
      case  33 => GrabKey(stream, data)
      case  34 => UngrabKey(stream, data)
      case  35 => AllowEvents(stream, data)
      case  36 => GrabServer
      case  37 => UngrabServer
      case  38 => QueryPointer(stream)
      case  39 => GetMotionEvents(stream)
      case  40 => TranslateCoordinates(stream)
      case  41 => WarpPointer(stream)
      case  42 => SetInputFocus(stream, data)
      case  43 => GetInputFocus
      case  44 => QueryKeymap
      case  45 => OpenFont(stream)
      case  46 => CloseFont(stream)
      case  47 => QueryFont(stream)
      case  48 => QueryTextExtents(stream, data, length)
      case  49 => ListFonts(stream)
      case  50 => ListFontsWithInfo(stream)
      case  51 => SetFontPath(stream)
      case  52 => GetFontPath
      case  53 => CreatePixmap(stream, data)
      case  54 => FreePixmap(stream)
      case  55 => CreateGC(stream)
      case  56 => ChangeGC(stream)
      case  57 => CopyGC(stream)
      case  58 => SetDashes(stream)
      case  59 => SetClipRectangles(stream, data, length)
      case  60 => FreeGC(stream)
      case  61 => ClearArea(stream, data)
      case  62 => CopyArea(stream)
      case  63 => CopyPlane(stream)
      case  64 => PolyPoint(stream, data, length)
      case  65 => PolyLine(stream, data, length)
      case  66 => PolySegment(stream, length)
      case  67 => PolyRectangle(stream, length)
      case  68 => PolyArc(stream, length)
      case  69 => FillPoly(stream, length)
      case  70 => PolyFillRectangle(stream, length)
      case  71 => PolyFillArc(stream, length)
      case  72 => PutImage(stream, data, length)
      case  73 => GetImage(stream, data)
      case  74 => PolyText8(stream, length)
      case  75 => PolyText16(stream, length)
      case  76 => ImageText8(stream, data)
      case  77 => ImageText16(stream, data)
      case  78 => CreateColormap(stream, data)
      case  79 => FreeColormap(stream)
      case  80 => CopyColormapAndFree(stream)
      case  81 => InstallColormap(stream)
      case  82 => UninstallColormap(stream)
      case  83 => ListInstalledColormaps(stream)
      case  84 => AllocColor(stream)
      case  85 => AllocNamedColor(stream)
      case  86 => AllocColorCells(stream, data)
      case  87 => AllocColorPlanes(stream, data)
      case  88 => FreeColors(stream, length)
      case  89 => StoreColors(stream, length)
      case  90 => StoreNamedColor(stream, data)
      case  91 => QueryColors(stream, length)
      case  92 => LookupColor(stream)
      case  93 => CreateCursor(stream)
      case  94 => CreateGlyphCursor(stream)
      case  95 => FreeCursor(stream)
      case  96 => RecolorCursor(stream)
      case  97 => QueryBestSize(stream, data)
      case  98 => QueryExtension(stream)
      case  99 => ListExtensions
      case 100 => ChangeKeyboardMapping(stream, data)
      case 101 => GetKeyboardMapping(stream)
      case 102 => ChangeKeyboardControl(stream)
      case 103 => GetKeyboardControl
      case 104 => Bell(stream, data)
      case 105 => ChangePointerControl(stream)
      case 106 => GetPointerControl
      case 107 => SetScreenSaver(stream)
      case 108 => GetScreenSaver
      case 109 => ChangeHosts(stream, data)
      case 110 => ListHosts
      case 111 => SetAccessControl(stream, data)
      case 112 => SetCloseDownMode(stream, data)
      case 113 => KillClient(stream)
      case 114 => RotateProperties(stream)
      case 115 => ForceScreenSaver(stream, data)
      case 116 => SetPointerMapping(stream, data)
      case 117 => GetPointerMapping
      case 118 => SetModifierMapping(stream, data)
      case 119 => GetModifierMapping
      case 127 => NoOperation(stream)
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
    val window = stream.readWindow()
    new GetWindowAttributes(window)
  }
}

case class DestroyWindow (
  val window: Window
  ) extends Request(4)

object DestroyWindow {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    new DestroyWindow(window)
  }
}

case class DestroySubWindows (
  val window: Window
  ) extends Request(5)

object DestroySubWindows {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    new DestroySubWindows(window)
  }
}

case class ChangeSaveSet (
  val mode: UInt8,
  val window: Window
  ) extends Request(6)

object ChangeSaveSet {
  def apply(stream: BinaryInputStream, mode: UInt8) = {
    val window = stream.readWindow()
    new ChangeSaveSet(mode, window)
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
    val window = stream.readWindow()
    new MapWindow(window)
  }
}

case class MapSubwindows (
  val window: Window
  ) extends Request(9)

object MapSubwindows {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    new MapSubwindows(window)
  }
}

case class UnmapWindow (
  val window: Window
  ) extends Request(10)

object UnmapWindow {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    new UnmapWindow(window)
  }
}

case class UnmapSubwindows (
  val window: Window
  ) extends Request(11)

object UnmapSubwindows {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    new UnmapSubwindows(window)
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
  val direction: Card8,
  val window: Window
  ) extends Request(13)

object CirculateWindow {
  def apply(stream: BinaryInputStream, direction: Card8) = {
    val window = stream.readWindow()
    new CirculateWindow(direction, window)
  }
}

case class GetGeometry (
  val drawable: Drawable
  ) extends Request(14)

object GetGeometry {
  def apply(stream: BinaryInputStream) = {
    val drawable = stream.readDrawable()
    new GetGeometry(drawable)
  }
}

case class QueryTree (
  val window: Window
  ) extends Request(15)

object QueryTree {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    new QueryTree(window)
  }
}

case class InternAtom (
  val onlyIfExists: Bool,
  val name: Str
  ) extends Request(16)

object InternAtom {
  def apply(stream: BinaryInputStream, onlyIfExists: Card8) = {
    val n = stream.readUInt16()
    stream.skipBytes(2)
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
    stream.skipBytes(3)
    val n = stream.readUInt32() * (format / 8)
    val data = mutable.MutableList[UInt8]()
    1 to n foreach { _ => data += stream.readUInt8() }
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
    val window = stream.readWindow()
    new ListProperties(window)
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
    val atom = stream.readAtom()
    new GetSelectionOwner(atom)
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
    stream.skipBytes(1)
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
    stream.skipBytes(2)
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
    stream.skipBytes(2)
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
    stream.skipBytes(2)
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
    stream.skipBytes(2)
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
    stream.skipBytes(2)
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
  def apply(stream: BinaryInputStream, oddLength: Card8, requestLength: Card16) = {
    val font = stream.readFontable()
    val stringAndPaddingLength = (requestLength - 2) * 4
    val n = if(oddLength.toBool.value) stringAndPaddingLength - 2
            else stringAndPaddingLength
    stream.readString16(n / 2)
    if(oddLength.toBool.value) stream.skipBytes(2)
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
    stream.skipBytes(2)
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
  val drawable: Drawable,
  val gc: GContext,
  val segments: List[Segment]
) extends Request(66)

object PolySegment {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val points = stream.readListOfSegments(n)
    new PolySegment(drawable, gc, points)
  }
}

case class PolyRectangle (
  val drawable: Drawable,
  val gc: GContext,
  val rectangles: List[Rectangle]
) extends Request(67)

object PolyRectangle {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val points = stream.readListOfRectangles(n)
    new PolyRectangle(drawable, gc, points)
  }
}

case class PolyArc (
  val drawable: Drawable,
  val gc: GContext,
  val arcs: List[Arc]
) extends Request(68)

object PolyArc {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val arcs = stream.readListOfArcs(n)
    new PolyArc(drawable, gc, arcs)
  }
}

case class FillPoly (
  val drawable: Drawable,
  val gc: GContext,
  val shape: Card8,
  val coordinateMode: Card8,
  val points: List[Point]
) extends Request(69)

object FillPoly {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val shape = stream.readCard8()
    val coordinateMode = stream.readCard8()
    stream.skipBytes(2)
    val n = requestLength - 4
    val points = stream.readListOfPoints(n)
    new FillPoly(drawable, gc, shape, coordinateMode, points)
  }
}

case class PolyFillRectangle (
  val drawable: Drawable,
  val gc: GContext,
  val rectangles: List[Rectangle]
) extends Request(70)


object PolyFillRectangle {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val points = stream.readListOfRectangles(n)
    new PolyFillRectangle(drawable, gc, points)
  }
}

case class PolyFillArc (
  val drawable: Drawable,
  val gc: GContext,
  val arcs: List[Arc]
) extends Request(71)

object PolyFillArc {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val n = requestLength - 3
    val arcs = stream.readListOfArcs(n)
    new PolyFillArc(drawable, gc, arcs)
  }
}

case class PutImage (
  val format: Card8,
  val drawable: Drawable,
  val gc: GContext,
  val width: Card16,
  val height: Card16,
  val dstX: Int16,
  val dstY: Int16,
  val depth: Card8,
  val data: List[Int8]
) extends Request(72)

object PutImage {
  def apply(stream: BinaryInputStream, format: Card8, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val width = stream.readCard16()
    val height = stream.readCard16()
    val dstX = stream.readInt16()
    val dstY = stream.readInt16()
    val leftPad = stream.readCard8()
    val depth = stream.readCard8()
    stream.skipBytes(2 + leftPad)
    val n = (requestLength - 6) * 4
    val data = stream.readListOfInt8(n)
    new PutImage(format, drawable, gc, width,
      height, dstX, dstY, depth, data)
  }
}

case class GetImage (
  val format: Card8,
  val drawable: Drawable,
  val x: Int16,
  val y: Int16,
  val width: Card16,
  val height: Card16,
  val planeMask: Card32
) extends Request(73)

object GetImage {
  def apply(stream: BinaryInputStream, format: Card8) = {
    val drawable = stream.readDrawable()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val width = stream.readCard16()
    val height = stream.readCard16()
    val planeMask = stream.readCard32()
    new GetImage(format, drawable, x, y, width, height, planeMask)
  }
}

case class PolyText8 (
  val drawable: Drawable,
  val gc: GContext,
  val x: Int16,
  val y: Int16,
  val items: List[TextItem]
) extends Request(74)

object PolyText8 {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val n = (requestLength - 4) * 4
    val items = stream.readListOfTextItem8(n)
    new PolyText8(drawable, gc, x, y, items)
  }
}

case class PolyText16 (
  val drawable: Drawable,
  val gc: GContext,
  val x: Int16,
  val y: Int16,
  val items: List[TextItem]
) extends Request(75)

object PolyText16 {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val n = (requestLength - 4) * 4
    val items = stream.readListOfTextItem16(n)
    new PolyText16(drawable, gc, x, y, items)
  }
}

case class ImageText8 (
  val drawable: Drawable,
  val gc: GContext,
  val x: Int16,
  val y: Int16,
  val string: Str
) extends Request(76)

object ImageText8 {
  def apply(stream: BinaryInputStream, n: Card8) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val string = stream.readString8(n)
    stream.readPad(n)
    new ImageText8(drawable, gc, x, y, string)
  }
}

case class ImageText16 (
  val drawable: Drawable,
  val gc: GContext,
  val x: Int16,
  val y: Int16,
  val string: Str
) extends Request(77)

object ImageText16 {
  def apply(stream: BinaryInputStream, n: Card8) = {
    val drawable = stream.readDrawable()
    val gc = stream.readGContext()
    val x = stream.readInt16()
    val y = stream.readInt16()
    val string = stream.readString16(n)
    stream.readPad((2 * n):Card8)
    new ImageText16(drawable, gc, x, y, string)
  }
}

case class CreateColormap (
  val alloc: Card8,
  val mid: Colormap,
  val window: Window,
  val visualId: VisualID
) extends Request(78)

object CreateColormap {
  def apply(stream: BinaryInputStream, alloc: Card8) = {
    val mid = stream.readColormap()
    val window = stream.readWindow()
    val visualId = stream.readVisualID()
    new CreateColormap(alloc, mid, window, visualId)
  }
}

case class FreeColormap (
  val cmap: Colormap
) extends Request(79)

object FreeColormap {
  def apply(stream: BinaryInputStream) = {
    val cmap = stream.readColormap()
    new FreeColormap(cmap)
  }
}

case class CopyColormapAndFree (
  val mid: Colormap,
  val srcCmap: Colormap
) extends Request(80)

object CopyColormapAndFree {
  def apply(stream: BinaryInputStream) = {
    val mid = stream.readColormap()
    val srcCmap = stream.readColormap()
    new CopyColormapAndFree(mid, srcCmap)
  }
}

case class InstallColormap (
  val cmap: Colormap
) extends Request(81)

object InstallColormap {
  def apply(stream: BinaryInputStream) = {
    val cmap = stream.readColormap()
    new InstallColormap(cmap)
  }
}

case class UninstallColormap (
  val cmap: Colormap
) extends Request(82)

object UninstallColormap {
  def apply(stream: BinaryInputStream) = {
    val cmap = stream.readColormap()
    new UninstallColormap(cmap)
  }
}

case class ListInstalledColormaps (
  val window: Window
) extends Request(83)

object ListInstalledColormaps {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    new ListInstalledColormaps(window)
  }
}

case class AllocColor (
  val cmap: Colormap,
  val red: Card16,
  val green: Card16,
  val blue: Card16
) extends Request(84)

object AllocColor {
  def apply(stream: BinaryInputStream) = {
    val cmap = stream.readColormap()
    val red = stream.readCard16()
    val green = stream.readCard16()
    val blue = stream.readCard16()
    stream.skipBytes(2)
    new AllocColor(cmap, red, green, blue)
  }
}

case class AllocNamedColor (
  val cmap: Colormap,
  val name: Str
) extends Request(85)

object AllocNamedColor {
  def apply(stream: BinaryInputStream) = {
    val cmap = stream.readColormap()
    val n = stream.readCard16()
    stream.skipBytes(2)
    val name = stream.readString8(n)
    stream.readPad(n)
    new AllocNamedColor(cmap, name)
  }
}

case class AllocColorCells (
  val contiguous: Bool,
  val cmap: Colormap,
  val colors: Card16,
  val planes: Card16
) extends Request(86)

object AllocColorCells {
  def apply(stream: BinaryInputStream, contiguous: Card8) = {
    val cmap = stream.readColormap()
    val colors = stream.readCard16()
    val planes = stream.readCard16()
    new AllocColorCells(contiguous.toBool, cmap, colors, planes)
  }
}

case class AllocColorPlanes (
  val contiguous: Bool,
  val cmap: Colormap,
  val colors: Card16,
  val reds: Card16,
  val greens: Card16,
  val blues: Card16
) extends Request(87)

object AllocColorPlanes {
  def apply(stream: BinaryInputStream, contiguous: Card8) = {
    val cmap = stream.readColormap()
    val colors = stream.readCard16()
    val reds = stream.readCard16()
    val greens = stream.readCard16()
    val blues = stream.readCard16()
    new AllocColorPlanes(contiguous, cmap, colors, reds, greens, blues)
  }
}

case class FreeColors (
  val cmap: Colormap,
  val planeMask: Card32,
  val pixels: List[Card32]
) extends Request(88)

object FreeColors {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val cmap = stream.readColormap()
    val planeMask = stream.readCard32()
    val n = requestLength - 3
    val pixels = stream.readListOfCard32(n)
    new FreeColors(cmap, planeMask, pixels)
  }
}

case class StoreColors (
  val cmap: Colormap,
  val items: List[ColorItem]
) extends Request(89)

object StoreColors {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val cmap = stream.readColormap()
    val n = requestLength - 2
    val items = stream.readListOfColorItems(n)
    new StoreColors(cmap, items)
  }
}

case class StoreNamedColor (
  val flag: Card8,
  val cmap: Colormap,
  val pixel: Card32,
  val name: Str
) extends Request(90)

object StoreNamedColor {
  def apply(stream: BinaryInputStream, flag: Card8) = {
    val cmap = stream.readColormap()
    val pixel = stream.readCard32()
    val n = stream.readCard8()
    stream.skipBytes(2)
    val name = stream.readString8(n)
    stream.readPad(n)
    new StoreNamedColor(flag, cmap, pixel, name)
  }
}

case class QueryColors(
  val cmap: Colormap,
  val pixels: List[Card32]
) extends Request(91)

object QueryColors {
  def apply(stream: BinaryInputStream, requestLength: Card16) = {
    val cmap = stream.readColormap()
    val n = requestLength - 2
    val pixels = stream.readListOfCard32(n)
    new QueryColors(cmap, pixels)
  }
}

case class LookupColor (
  val cmap: Colormap,
  val name: Str
) extends Request(92)

object LookupColor {
  def apply(stream: BinaryInputStream) = {
    val cmap = stream.readColormap()
    val n = stream.readCard16()
    stream.skipBytes(2)
    val name = stream.readString8(n)
    stream.readPad(n)
    new LookupColor(cmap, name)
  }
}

case class CreateCursor (
  val cid: Cursor,
  val source: Pixmap,
  val mask: Pixmap,
  val foreRed: Card16,
  val foreGreen: Card16,
  val foreBlue: Card16,
  val backRed: Card16,
  val backGreen: Card16,
  val backBlue: Card16,
  val x: Card16,
  val y: Card16
) extends Request(93)

object CreateCursor {
  def apply(stream: BinaryInputStream) = {
    val cid = stream.readCursor()
    val source = stream.readPixmap()
    val mask = stream.readPixmap()
    val foreRed = stream.readCard16()
    val foreGreen = stream.readCard16()
    val foreBlue = stream.readCard16()
    val backRed = stream.readCard16()
    val backGreen = stream.readCard16()
    val backBlue = stream.readCard16()
    val x = stream.readCard16()
    val y = stream.readCard16()
    new CreateCursor(cid, source, mask, foreRed, foreGreen,
      foreBlue, backRed, backGreen, backBlue, x, y)
  }
}

case class CreateGlyphCursor (
  val cid: Cursor,
  val sourceFont: Font,
  val maskFont: Font,
  val sourceChar: Card16,
  val maskChar: Card16,
  val foreRed: Card16,
  val foreGreen: Card16,
  val foreBlue: Card16,
  val backRed: Card16,
  val backGreen: Card16,
  val backBlue: Card16
) extends Request(94)

object CreateGlyphCursor {
  def apply(stream: BinaryInputStream) = {
    val cid = stream.readCursor()
    val sourceFont = stream.readFont()
    val maskFont = stream.readFont()
    val sourceChar = stream.readCard16()
    val maskChar = stream.readCard16()
    val foreRed = stream.readCard16()
    val foreGreen = stream.readCard16()
    val foreBlue = stream.readCard16()
    val backRed = stream.readCard16()
    val backGreen = stream.readCard16()
    val backBlue = stream.readCard16()
    new CreateGlyphCursor(cid, sourceFont, maskFont, sourceChar,
      maskChar, foreRed, foreGreen, foreBlue, backRed, backGreen, backBlue)
  }
}

case class FreeCursor (
  val cursor: Cursor
) extends Request(95)

object FreeCursor {
  def apply(stream: BinaryInputStream) = {
    val cursor = stream.readCursor()
    new FreeCursor(cursor)
  }
}

case class RecolorCursor (
  val cursor: Cursor,
  val foreRed: Card16,
  val foreGreen: Card16,
  val foreBlue: Card16,
  val backRed: Card16,
  val backGreen: Card16,
  val backBlue: Card16
) extends Request(96)

object RecolorCursor {
  def apply(stream: BinaryInputStream) = {
    val cursor = stream.readCursor()
    val foreRed = stream.readCard16()
    val foreGreen = stream.readCard16()
    val foreBlue = stream.readCard16()
    val backRed = stream.readCard16()
    val backGreen = stream.readCard16()
    val backBlue = stream.readCard16()
    new RecolorCursor(cursor, foreRed, foreGreen, foreBlue,
      backRed, backGreen, backBlue)
  }
}

case class QueryBestSize (
  val classType: Card8,
  val drawable: Drawable,
  val size: Size
) extends Request(97)

object QueryBestSize {
  def apply(stream: BinaryInputStream, classType: Card8) = {
    val drawable = stream.readDrawable()
    val size = stream.readSize()
    new QueryBestSize(classType, drawable, size)
  }
}

case class QueryExtension (
  val name: Str
) extends Request(98)

object QueryExtension {
  def apply(stream: BinaryInputStream) = {
    val n = stream.readCard8()
    stream.skipBytes(2)
    val name = stream.readString8(n)
    stream.readPad(n)
    new QueryExtension(name)
  }
}

case object ListExtensions extends Request(99)

case class ChangeKeyboardMapping (
  val firstKeycode: Keycode,
  val keysyms: List[Keysym]
) extends Request(100)

object ChangeKeyboardMapping {
  def apply(stream: BinaryInputStream, keycodeCount: Card8) = {
    val firstKeycode = stream.readKeycode()
    val keysymsPerKeycode = stream.readCard8()
    stream.skipBytes(2)
    val numberOfKeysyms = keycodeCount * keysymsPerKeycode
    val keysyms = stream.readListOfKeysyms(numberOfKeysyms)
    new ChangeKeyboardMapping(firstKeycode, keysyms)
  }
}

case class GetKeyboardMapping (
  val firstKeycode: Keycode,
  val count: Card8
) extends Request(101)

object GetKeyboardMapping {
  def apply(stream: BinaryInputStream) = {
    val firstKeycode = stream.readKeycode()
    val count = stream.readCard8()
    stream.skipBytes(2)
    new GetKeyboardMapping(firstKeycode, count)
  }
}

case class ChangeKeyboardControl (
  val values: Map[String, Value]
) extends Request(102)

object ChangeKeyboardControl {
  def apply(stream: BinaryInputStream) = {
    val bitmask = stream.readBitmask()
    var values = mutable.Map[String, Value]()
    if((bitmask & 0x0001) != 0) values += ("keyClickPercent" -> stream.readInt8())
    if((bitmask & 0x0002) != 0) values += ("bellPercent" -> stream.readInt8())
    if((bitmask & 0x0004) != 0) values += ("bellPitch" -> stream.readInt16())
    if((bitmask & 0x0008) != 0) values += ("bellDuration" -> stream.readInt16())
    if((bitmask & 0x0010) != 0) values += ("led" -> stream.readCard8())
    if((bitmask & 0x0020) != 0) values += ("ledMode" -> stream.readCard8())
    if((bitmask & 0x0040) != 0) values += ("key" -> stream.readKeycode())
    if((bitmask & 0x0080) != 0) values += ("autoRepeatMode" -> stream.readCard8())
    new ChangeKeyboardControl(Map(values.toStream: _*))
  }
}


case object GetKeyboardControl extends Request(103)

case class Bell (
  val percent: Int8
) extends Request(104)

object Bell {
  def apply(stream: BinaryInputStream, percent: Card8) = {
    new Bell(percent.toInt8)
  }
}

case class ChangePointerControl (
  val accelerationNumerator: Int16,
  val accelerationDenominator: Int16,
  val threshold: Int16,
  val doAcceleration: Bool,
  val doThreshold: Bool
) extends Request(105)

object ChangePointerControl {
  def apply(stream: BinaryInputStream) = {
    val accelerationNumerator = stream.readInt16()
    val accelerationDenominator = stream.readInt16()
    val threshold = stream.readInt16()
    val doAcceleration = stream.readBool()
    val doThreshold = stream.readBool()
    new ChangePointerControl(accelerationNumerator,
      accelerationDenominator, threshold, doAcceleration, doThreshold)
  }
}

case object GetPointerControl extends Request(106)

case class SetScreenSaver (
  val timeout: Int16,
  val interval: Int16,
  val preferBlanking: Card8,
  val allowExposures: Card8
) extends Request(107)

object SetScreenSaver {
  def apply(stream: BinaryInputStream) = {
    val timeout = stream.readInt16()
    val interval = stream.readInt16()
    val preferBlanking = stream.readCard8()
    val allowExposures = stream.readCard8()
    new SetScreenSaver(timeout, interval, preferBlanking, allowExposures)
  }
}

case object GetScreenSaver extends Request(108)

case class ChangeHosts (
  val mode: Card8,
  val host: Host
) extends Request(109)

object ChangeHosts {
  def apply(stream: BinaryInputStream, mode: Card8) = {
    val host = stream.readHost()
    new ChangeHosts(mode, host)
  }
}

case object ListHosts extends Request(110)

case class SetAccessControl (
  val mode: Card8
) extends Request(111)

object SetAccessControl {
  def apply(stream: BinaryInputStream, mode: Card8) = {
    new SetAccessControl(mode)
  }
}

case class SetCloseDownMode (
  val mode: Card8
) extends Request(112)

object SetCloseDownMode {
  def apply(stream: BinaryInputStream, mode: Card8) = {
    new SetCloseDownMode(mode)
  }
}

case class KillClient (
  val resource: Card32
) extends Request(113)

object KillClient {
  def apply(stream: BinaryInputStream) = {
    val resource = stream.readCard32()
    new KillClient(resource)
  }
}

case class RotateProperties (
  val window: Window,
  val delta: Int16,
  val properties: List[Atom]
) extends Request(114)

object RotateProperties {
  def apply(stream: BinaryInputStream) = {
    val window = stream.readWindow()
    val n = stream.readCard16()
    val delta = stream.readInt16()
    val properties = stream.readListOfAtoms(n)
    new RotateProperties(window, delta, properties)
  }
}

case class ForceScreenSaver (
  val mode: Card8
) extends Request(115)

object ForceScreenSaver {
  def apply(stream: BinaryInputStream, mode: Card8) = {
    new ForceScreenSaver(mode)
  }
}

case class SetPointerMapping (
  val map: List[Card8]
) extends Request(116)

object SetPointerMapping {
  def apply(stream: BinaryInputStream, n: Card8) = {
    val map = stream.readListOfCard8(n)
    stream.readPad(n)
    new SetPointerMapping(map)
  }
}

case object GetPointerMapping extends Request(117)

case class SetModifierMapping (
  val keycodes: List[Keycode]
) extends Request(118)

object SetModifierMapping {
  def apply(stream: BinaryInputStream, keycodesPerModifier: Card8) = {
    val modifiersNumber = 8
    val keycodes = stream.readListOfKeycode(keycodesPerModifier * modifiersNumber)
    new SetModifierMapping(keycodes)
  }
}

case object GetModifierMapping extends Request(119)

case object NoOperation extends Request(127) {
  def apply(stream: BinaryInputStream) = {
    val length = stream.readCard16()
    val n = length - 1
    stream.skipBytes(n * 4)
  }
}

