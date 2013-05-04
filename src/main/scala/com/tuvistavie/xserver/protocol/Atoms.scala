package com.tuvistavie.xserver.protocol.atoms

abstract sealed class BaseAtom(val value: Int)

case object Primary extends BaseAtom(1)
case object Secondary extends BaseAtom(2)
case object Arc extends BaseAtom(3)
case object Atom extends BaseAtom(4)
case object Bitmap extends BaseAtom(5)
case object Cardinal extends BaseAtom(6)
case object Colormap extends BaseAtom(7)
case object Cursor extends BaseAtom(8)
case object CutBuffer0 extends BaseAtom(9)
case object CutBuffer1 extends BaseAtom(10)
case object CutBuffer2 extends BaseAtom(11)
case object CutBuffer3 extends BaseAtom(12)
case object CutBuffer4 extends BaseAtom(13)
case object CutBuffer5 extends BaseAtom(14)
case object CutBuffer6 extends BaseAtom(15)
case object CutBuffer7 extends BaseAtom(16)
case object Drawable extends BaseAtom(17)
case object Font extends BaseAtom(18)
case object Integer extends BaseAtom(19)
case object Pixmap extends BaseAtom(20)
case object Point extends BaseAtom(21)
case object Rectangle extends BaseAtom(22)
case object ResourceManager extends BaseAtom(23)
case object RgbColorMap extends BaseAtom(24)
case object RgbBestMap extends BaseAtom(25)
case object RgbBlueMap extends BaseAtom(26)
case object RgbDefaultMap extends BaseAtom(27)
case object RgbGrayMap extends BaseAtom(28)
case object RgbGreenMap extends BaseAtom(29)
case object RgbRedMap extends BaseAtom(30)
case object String extends BaseAtom(31)
case object Visualid extends BaseAtom(32)
case object Window extends BaseAtom(33)
case object WmCommand extends BaseAtom(34)
case object WmHints extends BaseAtom(35)
case object WmClientMachine extends BaseAtom(36)
case object WmIconName extends BaseAtom(37)
case object WmIconSize extends BaseAtom(38)
case object WmName extends BaseAtom(39)
case object WmNormalHints extends BaseAtom(40)
case object WmSizeHints extends BaseAtom(41)
case object WmZoomHints extends BaseAtom(42)
case object MinSpace extends BaseAtom(43)
case object NormSpace extends BaseAtom(44)
case object MaxSpace extends BaseAtom(45)
case object EndSpace extends BaseAtom(46)
case object SuperscriptX extends BaseAtom(47)
case object SuperscriptY extends BaseAtom(48)
case object SubscriptX extends BaseAtom(49)
case object SubscriptY extends BaseAtom(50)
case object UnderlinePosition extends BaseAtom(51)
case object UnderlineThickness extends BaseAtom(52)
case object StrikeoutAscent extends BaseAtom(53)
case object StrikeoutDescent extends BaseAtom(54)
case object ItalicAngle extends BaseAtom(55)
case object XHeight extends BaseAtom(56)
case object QuadWidth extends BaseAtom(57)
case object Weight extends BaseAtom(58)
case object PointSize extends BaseAtom(59)
case object Resolution extends BaseAtom(60)
case object Copyright extends BaseAtom(61)
case object Notice extends BaseAtom(62)
case object FontName extends BaseAtom(63)
case object FamilyName extends BaseAtom(64)
case object FullName extends BaseAtom(65)
case object CapHeight extends BaseAtom(66)
case object WmClass extends BaseAtom(67)
case object WmTransientFor extends BaseAtom(68)

object BaseAtom {
  def fromValue(value: Int) = value match {
    case Primary.value => Primary
    case Secondary.value => Secondary
    case Arc.value => Arc
    case Atom.value => Atom
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
  }
}

