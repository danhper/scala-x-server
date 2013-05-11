package com.tuvistavie.xserver.protocol.types

import com.tuvistavie.util.Enumerable

abstract sealed class Atom(override val value: Int) extends UInt32(value) {
  def toUInt32 = UInt32(value)
}

object Atom extends Enumerable[Atom] {
  import atoms._
  def fromValue(value: Int) = value match {
    case Primary.value => Primary
    case Secondary.value => Secondary
    case Arc.value => Arc
    case AtomObj.value => AtomObj
    case Bitmap.value => Bitmap
    case Cardinal.value => Cardinal
    case Colormap.value => Colormap
    case Cursor.value => Cursor
    case CutBuffer0.value => CutBuffer0
    case CutBuffer1.value => CutBuffer1
    case CutBuffer2.value => CutBuffer2
    case CutBuffer3.value => CutBuffer3
    case CutBuffer4.value => CutBuffer4
    case CutBuffer5.value => CutBuffer5
    case CutBuffer6.value => CutBuffer6
    case CutBuffer7.value => CutBuffer7
    case Drawable.value => Drawable
    case Font.value => Font
    case Integer.value => Integer
    case Pixmap.value => Pixmap
    case Point.value => Point
    case Rectangle.value => Rectangle
    case ResourceManager.value => ResourceManager
    case RgbColorMap.value => RgbColorMap
    case RgbBestMap.value => RgbBestMap
    case RgbBlueMap.value => RgbBlueMap
    case RgbDefaultMap.value => RgbDefaultMap
    case RgbGrayMap.value => RgbGrayMap
    case RgbGreenMap.value => RgbGreenMap
    case RgbRedMap.value => RgbRedMap
    case String.value => String
    case Visualid.value => Visualid
    case Window.value => Window
    case WmCommand.value => WmCommand
    case WmHints.value => WmHints
    case WmClientMachine.value => WmClientMachine
    case WmIconName.value => WmIconName
    case WmIconSize.value => WmIconSize
    case WmName.value => WmName
    case WmNormalHints.value => WmNormalHints
    case WmSizeHints.value => WmSizeHints
    case WmZoomHints.value => WmZoomHints
    case MinSpace.value => MinSpace
    case NormSpace.value => NormSpace
    case MaxSpace.value => MaxSpace
    case EndSpace.value => EndSpace
    case SuperscriptX.value => SuperscriptX
    case SuperscriptY.value => SuperscriptY
    case SubscriptX.value => SubscriptX
    case SubscriptY.value => SubscriptY
    case UnderlinePosition.value => UnderlinePosition
    case UnderlineThickness.value => UnderlineThickness
    case StrikeoutAscent.value => StrikeoutAscent
    case StrikeoutDescent.value => StrikeoutDescent
    case ItalicAngle.value => ItalicAngle
    case XHeight.value => XHeight
    case QuadWidth.value => QuadWidth
    case Weight.value => Weight
    case PointSize.value => PointSize
    case Resolution.value => Resolution
    case Copyright.value => Copyright
    case Notice.value => Notice
    case FontName.value => FontName
    case FamilyName.value => FamilyName
    case FullName.value => FullName
    case CapHeight.value => CapHeight
    case WmClass.value => WmClass
    case WmTransientFor.value => WmTransientFor
    case _ => Nothing
  }
}

package atoms {
  case object Nothing extends Atom(0)
  case object Primary extends Atom(1)
  case object Secondary extends Atom(2)
  case object Arc extends Atom(3)
  case object AtomObj extends Atom(4)
  case object Bitmap extends Atom(5)
  case object Cardinal extends Atom(6)
  case object Colormap extends Atom(7)
  case object Cursor extends Atom(8)
  case object CutBuffer0 extends Atom(9)
  case object CutBuffer1 extends Atom(10)
  case object CutBuffer2 extends Atom(11)
  case object CutBuffer3 extends Atom(12)
  case object CutBuffer4 extends Atom(13)
  case object CutBuffer5 extends Atom(14)
  case object CutBuffer6 extends Atom(15)
  case object CutBuffer7 extends Atom(16)
  case object Drawable extends Atom(17)
  case object Font extends Atom(18)
  case object Integer extends Atom(19)
  case object Pixmap extends Atom(20)
  case object Point extends Atom(21)
  case object Rectangle extends Atom(22)
  case object ResourceManager extends Atom(23)
  case object RgbColorMap extends Atom(24)
  case object RgbBestMap extends Atom(25)
  case object RgbBlueMap extends Atom(26)
  case object RgbDefaultMap extends Atom(27)
  case object RgbGrayMap extends Atom(28)
  case object RgbGreenMap extends Atom(29)
  case object RgbRedMap extends Atom(30)
  case object String extends Atom(31)
  case object Visualid extends Atom(32)
  case object Window extends Atom(33)
  case object WmCommand extends Atom(34)
  case object WmHints extends Atom(35)
  case object WmClientMachine extends Atom(36)
  case object WmIconName extends Atom(37)
  case object WmIconSize extends Atom(38)
  case object WmName extends Atom(39)
  case object WmNormalHints extends Atom(40)
  case object WmSizeHints extends Atom(41)
  case object WmZoomHints extends Atom(42)
  case object MinSpace extends Atom(43)
  case object NormSpace extends Atom(44)
  case object MaxSpace extends Atom(45)
  case object EndSpace extends Atom(46)
  case object SuperscriptX extends Atom(47)
  case object SuperscriptY extends Atom(48)
  case object SubscriptX extends Atom(49)
  case object SubscriptY extends Atom(50)
  case object UnderlinePosition extends Atom(51)
  case object UnderlineThickness extends Atom(52)
  case object StrikeoutAscent extends Atom(53)
  case object StrikeoutDescent extends Atom(54)
  case object ItalicAngle extends Atom(55)
  case object XHeight extends Atom(56)
  case object QuadWidth extends Atom(57)
  case object Weight extends Atom(58)
  case object PointSize extends Atom(59)
  case object Resolution extends Atom(60)
  case object Copyright extends Atom(61)
  case object Notice extends Atom(62)
  case object FontName extends Atom(63)
  case object FamilyName extends Atom(64)
  case object FullName extends Atom(65)
  case object CapHeight extends Atom(66)
  case object WmClass extends Atom(67)
  case object WmTransientFor extends Atom(68)
}
