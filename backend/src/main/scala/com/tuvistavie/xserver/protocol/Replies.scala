package com.tuvistavie.xserver.backend.protocol.replies

import com.tuvistavie.xserver.backend.io._
import com.tuvistavie.xserver.backend.protocol.types._

abstract class Reply(
  val data: Option[Card8],
  val sequenceNumber: Card16,
  val length: Card32
) {
  def write(stream: BinaryOutputStream): Unit
  def write(stream: BinaryOutputStream, values: Value*) = {
    stream.writeByte(1)
    data match {
      case Some(n) => stream.writeByte(n)
      case _ => stream.writeByte(0)
    }
    stream.writeUInt16(sequenceNumber)
    stream.writeUInt32(length)

    values.foreach(stream.writeValue _)

    if(length.value == 0) {
      stream.fill(unusedBytesNumber(values: _*))
    }
  }
  def unusedBytesNumber(values: Value*) = {
    24 - values./: (0) (_+_.byteSize)
  }
}

case class GetWindowAttributes (
  val backingStore: Card8,
  override val sequenceNumber: Card16,
  val visual: VisualID,
  val className: Card8,
  val bitGravity: BitGravity,
  val windowGravity: WindowGravity,
  val backingPlanes: Card32,
  val backingPixels: Card32,
  val saveUnder: Bool,
  val mapIsInstalled: Bool,
  val mapState: Card8,
  val overrideRedirect: Bool,
  val colorMap: Colormap,
  val allEventMasks: SetOfEvent,
  val yourEventMask: SetOfEvent,
  val doNotPropagateMask: SetOfDeviceEvent
) extends Reply(Some(backingStore), sequenceNumber, 3) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, visual, className, bitGravity, windowGravity,
      backingPlanes, backingPixels, saveUnder, mapIsInstalled,
      mapState, overrideRedirect, colorMap, allEventMasks,
      yourEventMask, doNotPropagateMask)
    stream.fill(2)
  }
}

case class GetGeometry (
  val depth: Card8,
  override val sequenceNumber: Card16,
  val root: Window,
  val x: Int16,
  val y: Int16,
  val width: Card16,
  val height: Card16,
  val borderWidth: Card16
) extends Reply(Some(depth), sequenceNumber, 0) {
  def write(stream: BinaryOutputStream) = {
    super.write(stream, root, x, y, width, height, borderWidth)
  }
}

case class QueryTree (
  override val sequenceNumber: Card16,
  val root: Window,
  val parent: Window,
  val numberOfWindows: Card16,
  val children: List[Window]
) extends Reply(None, sequenceNumber, numberOfWindows) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, root, parent, numberOfWindows)
    stream.fill(14)
    children foreach { stream.writeWindow(_) }
  }
}

case class InternAtom (
  override val sequenceNumber: Card16,
  val atom: Atom
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, atom)
  }
}

case class GetAtomName (
  override val sequenceNumber: Card16,
  val nameLength: Card16,
  val name: Str
) extends Reply(None, sequenceNumber, (nameLength.value + nameLength.padding) / 4) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, nameLength)
    stream.writeString8(name)
    stream.writePad(nameLength)
  }
}

case class GetProperty (
  val format: Card8,
  override val sequenceNumber: Card16,
  val typeName: Atom,
  val bytesAfter: Card32,
  val bytes: List[Card8]
) extends Reply(Some(format), sequenceNumber, (UInt32(bytes.length).padding + bytes.length) / 4) {
  override def write(stream: BinaryOutputStream) = {
    val lengthWithFormat: Card32 = if(format.value == 0) 0 else (bytes.length * 8) / format
    super.write(stream, typeName, bytesAfter, lengthWithFormat)
    if(bytes.length > 0) {
      stream.fill(12)
      stream.writeCard8List(bytes)
      stream.writePad(bytes.length)
    }
  }
}

case class ListProperties (
  override val sequenceNumber: Card16,
  val atoms: List[Atom]
) extends Reply(None, sequenceNumber, atoms.length) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt32(atoms.length))
    if(atoms.length > 0) {
      stream.fill(22)
      stream.writeAtomList(atoms)
    }
  }
}

case class GetSelectionOwner (
  override val sequenceNumber: Card16,
  val owner: Window
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, owner)
  }
}

case class GrabPointer (
  val status: Card8,
  override val sequenceNumber: Card16
) extends Reply(Some(status), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
  }
}

case class GrabKeyboard (
  val status: Card8,
  override val sequenceNumber: Card16
) extends Reply(Some(status), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
  }
}

case class QueryPointer (
  val sameScreen: Card8,
  override val sequenceNumber: Card16,
  val root: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val winX: Int16,
  val winY: Int16,
  val mask: SetOfKeyButMask
) extends Reply(Some(sameScreen), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, root, child, rootX, rootY, winX, winY, mask)
  }
}

case class GetMotionEvents (
  override val sequenceNumber: Card16,
  val events: List[TimeCoord]
) extends Reply(None, sequenceNumber, events.length * 2) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt32(events.length))
    if(events.length > 0) {
      stream.fill(20)
      stream.writeTimeCoordList(events)
    }
  }
}

case class TranslateCoordinates (
  val sameScreen: Card8,
  override val sequenceNumber: Card16,
  val child: Window,
  val dstX: Int16,
  val dstY: Int16
) extends Reply(Some(sameScreen), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, child, dstX, dstY)
  }
}


case class GetInputFocus (
  val revertTo: Card8,
  override val sequenceNumber: Card16,
  val focus: Window
) extends Reply(Some(revertTo), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, focus)
  }
}

case class QueryKeyMap (
  override val sequenceNumber: Card16,
  val keys: List[Card8]
) extends Reply(None, sequenceNumber, 2) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, sequenceNumber)
    stream.writeCard8List(keys)
  }
}

case class QueryFont (
  override val sequenceNumber: Card16,
  val minBounds: CharInfo,
  val maxBounds: CharInfo,
  val minCharOrByte2: Card16,
  val maxCharOrByte2: Card16,
  val defaultChar: Card16,
  val minByte1: Card8,
  val maxByte1: Card8,
  val allCharExist: Bool,
  val fontAscent: Int16,
  val fontDescent: Int16,
  val properties: List[FontProp],
  val charInfos: List[CharInfo]
) extends Reply(None, sequenceNumber, 7 + 2 * properties.length + 3 * charInfos.length) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
    stream.writeCharInfo(minBounds)
    stream.fill(4)
    stream.writeCharInfo(maxBounds)
    stream.fill(4)
    stream.writeCard16(minCharOrByte2)
    stream.writeCard16(maxCharOrByte2)
    stream.writeCard16(defaultChar)
    stream.writeCard16(properties.length)
    stream.writeCard8(minByte1)
    stream.writeCard8(maxByte1)
    stream.writeBool(allCharExist)
    stream.writeInt16(fontAscent)
    stream.writeInt16(fontDescent)
    stream.writeCard32(charInfos.length)
    stream.writeFontPropList(properties)
    stream.writeCharInfoList(charInfos)
  }
}

case class QueryTextExtents (
  val drawDirection: Card8,
  override val sequenceNumber: Card16,
  val fontAscent: Int16,
  val fontDescent: Int16,
  val overallAscent: Int16,
  val overallDescent: Int16,
  val overallWidth: Int32,
  val overallHeigth: Int32,
  val overallLeft: Int32,
  val overallRight: Int32
) extends Reply(Some(drawDirection), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, fontAscent, fontDescent, overallAscent,
      overallDescent, overallWidth, overallHeigth, overallLeft, overallRight)
  }
}

case class ListFonts (
  override val sequenceNumber: Card16,
  val names: List[Str]
) extends Reply(None, sequenceNumber, (names.length + UInt32(names.length).padding) / 4) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt16(names.length))
    if(names.length > 0) {
      stream.fill(22)
      stream.writeStrList(names)
      stream.writePad(names.length)
    }
  }
}

case class ListFontsWithInfo (
  val n: Card8,
  override val sequenceNumber: Card16,
  val minBounds: CharInfo,
  val maxBounds: CharInfo,
  val minCharOrByte2: Card16,
  val maxCharOrByte2: Card16,
  val defaultChar: Card16,
  val drawDirection: Card8,
  val minByte1: Card8,
  val maxByte1: Card8,
  val allCharExist: Bool,
  val fontAscent: Int16,
  val fontDescent: Int16,
  val repliesHint: Card32,
  val properties: List[FontProp],
  val name: Str
) extends Reply(Some(n), sequenceNumber, 7 + 2 * properties.length + (n.value + n.padding) / 4) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
    stream.writeCharInfo(minBounds)
    stream.fill(4)
    stream.writeCharInfo(maxBounds)
    stream.fill(4)
    stream.writeCard16(minCharOrByte2)
    stream.writeCard16(maxCharOrByte2)
    stream.writeCard16(defaultChar)
    stream.writeCard16(properties.length)
    stream.writeCard8(drawDirection)
    stream.writeCard8(minByte1)
    stream.writeCard8(maxByte1)
    stream.writeBool(allCharExist)
    stream.writeInt16(fontAscent)
    stream.writeInt16(fontDescent)
    stream.writeCard32(repliesHint)
    stream.writeFontPropList(properties)
    stream.writeStr(name)
    stream.writePad(n)
  }
}

object ListFontsWithInfo {
  def writeFinal(stream: BinaryOutputStream, sequenceNumber: Card16) = {
    stream.writeCard8(1)
    stream.writeCard8(0)
    stream.writeCard16(sequenceNumber)
    stream.writeCard32(7)
    stream.fill(52)
  }
}

case class GetFontPath (
  override val sequenceNumber: Card16,
  val path: List[Str]
) extends Reply(None, sequenceNumber, (path.length + UInt32(path.length).padding) / 4) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt16(path.length))
    if(path.length > 0) {
      stream.fill(22)
      stream.writeStrList(path)
      stream.writePad(path.length)
    }
  }
}

case class GetImage (
  val depth: Card8,
  override val sequenceNumber: Card16,
  val visual: VisualID,
  val imageData: List[Int8]
) extends Reply(Some(depth), sequenceNumber, (imageData.length + UInt32(imageData.length) / 4)) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, visual)
    if(imageData.length > 0) {
      stream.fill(20)
      stream.writeInt8List(imageData)
      stream.writePad(imageData.length)
    }
  }
}

case class ListInstalledColormaps (
  override val sequenceNumber: Card16,
  val cmaps: List[Colormap]
) extends Reply(None, sequenceNumber, cmaps.length) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt16(cmaps.length))
    if(cmaps.length > 0) {
      stream.fill(22)
      stream.writeCard32List(cmaps)
    }
  }
}

case class AllocColor (
  override val sequenceNumber: Card16,
  val red: Card16,
  val green: Card16,
  val blue: Card16,
  val pixel: Card32
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, red, green, blue, UInt16(0), pixel)
  }
}

case class AllocNamedColor (
  override val sequenceNumber: Card16,
  val pixel: Card32,
  val exactRed: Card16,
  val exactGreen: Card16,
  val exactBlue: Card16,
  val visualRed: Card16,
  val visualGreen: Card16,
  val visualBlue: Card16
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, pixel, exactRed, exactGreen, exactBlue,
      visualRed, visualGreen, visualBlue)
  }
}

case class AllocColorCells (
  override val sequenceNumber: Card16,
  val pixels: List[Card32],
  val masks: List[Card32]
) extends Reply(None, sequenceNumber, pixels.length + masks.length) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt16(pixels.length), UInt16(masks.length))
    if(pixels.length > 0 || masks.length > 0 ) {
      stream.fill(20)
      stream.writeCard32List(pixels)
      stream.writeCard32List(masks)
    }
  }
}

case class AllocColorPlanes (
  override val sequenceNumber: Card16,
  val redMask: Card32,
  val greenMask: Card32,
  val blueMask: Card32,
  val pixels: List[Card32]
) extends Reply(None, sequenceNumber, pixels.length) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt16(pixels.length), UInt16(0), redMask, greenMask, blueMask)
    if(pixels.length > 0) {
      stream.fill(8)
      stream.writeCard32List(pixels)
    }
  }
}

case class QueryColors (
  override val sequenceNumber: Card16,
  val colors: List[RGB]
) extends Reply(None, sequenceNumber, 2 * colors.length) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt16(colors.length))
    if(colors.length > 0) {
      stream.fill(22)
      stream.writeRGBList(colors)
    }
  }
}

case class LookupColor (
  override val sequenceNumber: Card16,
  val exactRed: Card16,
  val exactGreen: Card16,
  val exactBlue: Card16,
  val visualRed: Card16,
  val visualGreen: Card16,
  val visualBlue: Card16
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, exactRed, exactGreen, exactBlue,
      visualRed, visualGreen, visualBlue)
  }
}

case class QueryBestSize (
  override val sequenceNumber: Card16,
  val width: Card16,
  val height: Card16
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, width, height)
  }
}

case class QueryExtension (
  override val sequenceNumber: Card16,
  val present: Bool,
  val majorOpcode: Card8,
  val firstEvent: Card8,
  val firstError: Card8
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, present, majorOpcode, firstEvent, firstError)
  }
}

case class ListExtensions (
  override val sequenceNumber: Card16,
  val names: List[Str]
) extends Reply(Some(names.length), sequenceNumber, (names.length + UInt32(names.length).padding) / 4) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
    if(names.length > 0) {
      stream.fill(24)
      stream.writeStrList(names)
      stream.writePad(names.length)
    }
  }
}

case class GetKeyboardMaping (
  val keysymsPerKeycode: Card8,
  override val sequenceNumber: Card16,
  val keysyms: List[Keysym]
) extends Reply(Some(keysymsPerKeycode), sequenceNumber, keysyms.length * keysymsPerKeycode) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
    if(keysyms.length > 0) {
      stream.fill(24)
      stream.writeCard32List(keysyms)
    }
  }
}

case class GetKeyboardControl (
  val globalAutoRepeat: Card8,
  override val sequenceNumber: Card16,
  val ledMask: Card32,
  val keyClickPercent: Card8,
  val bellPercent: Card8,
  val bellPitch: Card16,
  val bellDuration: Card16,
  val autoRepeats: List[Card8]
) extends Reply(Some(globalAutoRepeat), sequenceNumber, 5) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, ledMask, keyClickPercent, bellPercent,
      bellPitch, bellDuration)
    stream.fill(2)
    stream.writeCard8List(autoRepeats)
  }
}

case class GetPointerControl (
  override val sequenceNumber: Card16,
  val accelerationNumerator: Card16,
  val accelerationDenominator: Card16,
  val threshold: Card16
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, accelerationNumerator, accelerationDenominator, threshold)
  }
}

case class GetScreenSaver (
  override val sequenceNumber: Card16,
  val timeout: Card16,
  val interval: Card16,
  val preferBlanking: Card8,
  val allowExposures: Card8
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, timeout, interval, preferBlanking, allowExposures)
  }
}

case class ListHosts (
  val mode: Card8,
  override val sequenceNumber: Card16,
  val hosts: List[Host]
) extends Reply(Some(mode), sequenceNumber, hosts.length / 2) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt16(hosts.length))
    if(hosts.length > 0) {
      stream.fill(22)
      stream.writeHostList(hosts)
    }
  }
}

case class SetPointerMapping (
  val status: Card8,
  override val sequenceNumber: Card16
) extends Reply(Some(status), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
  }
}

case class GetPointerMapping (
  val status: Card8,
  override val sequenceNumber: Card16,
  val map: List[Card8]
) extends Reply(Some(status), sequenceNumber, (map.length + UInt32(map.length).padding) / 4) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
    if(map.length > 0) {
      stream.fill(24)
      stream.writeCard8List(map)
      stream.writePad(map.length)
    }
  }
}

case class SetModifierMapping (
  val status: Card8,
  override val sequenceNumber: Card16
) extends Reply(Some(status), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
  }
}

case class GetModifierMapping (
  val keycodesPerModifier: Card8,
  override val sequenceNumber: Card16,
  val keycodes: List[Keycode]
) extends Reply(Some(keycodesPerModifier), sequenceNumber, 2 * keycodesPerModifier) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
    if(keycodes.length > 0) {
      stream.fill(24)
      stream.writeCard8List(keycodes)
    }
  }
}
