package com.tuvistavie.xserver.model

import akka.util.ByteString
import com.tuvistavie.xserver.util.ExtendedByteStringBuilder
import com.tuvistavie.xserver.util.Computations

class Screen (
  val root: Int,
  val colorMap: Int,
  val whitePixel: Int,
  val blackPixel: Int,
  val currentInputMasks: Int,
  val widthInPixels: Int,
  val heightInPixels: Int,
  val minInstalledMaps: Int,
  val maxInstalledMaps: Int,
  val rootVisual: Int,
  val backingStores: BackingStores,
  val saveUnder: Boolean,
  val rootDepth: Int,
  val allowedDepths: List[Depth]
) {
  def widthInMm: Int = Computations.pixelsToMillimeters(widthInPixels)
  def heightInMm: Int = Computations.pixelsToMillimeters(heightInPixels)

  def toByteString(implicit endian: java.nio.ByteOrder): ByteString = {
    import ExtendedByteStringBuilder._
    val builder = ByteString.newBuilder
    builder.putInt(root)
    builder.putInt(colorMap)
    builder.putInt(whitePixel)
    builder.putInt(blackPixel)
    builder.putInt(currentInputMasks)
    builder.putShort(widthInPixels)
    builder.putShort(heightInPixels)
    builder.putShort(widthInMm)
    builder.putShort(heightInMm)
    builder.putShort(minInstalledMaps)
    builder.putShort(maxInstalledMaps)
    builder.putInt(rootVisual)
    builder.putByte(backingStores.id().toByte)
    builder.putBoolean(saveUnder)
    builder.putByte(rootDepth.toByte)
    builder.putByte(allowedDepths.length.toByte)
    val depths = allowedDepths map { _.toByteString } reduce (_++_)
    builder.result ++ depths
  }
}

object Screen {
  val main: Screen = getFromConfig

  private[this] def getFromConfig = {
    new Screen(1,2,3)
  }
}

class Depth (
  val depth: Int,
  val visuals: List[VisualType]
) {
  def toByteString(implicit endian: java.nio.ByteOrder): ByteString = {
    import ExtendedByteStringBuilder._
    val builder = ByteString.newBuilder
    builder.putByte(depth toByte)
    builder.fill(1)
    builder.putShort(visuals.length toShort)
    builder.fill(4)
    val visualsByteString = visuals map { _.toByteString } reduce (_++_)
    builder.result ++ visualsByteString
  }
}

class VisualType (
  val visualId: Int,
  val colorClass: ColorClass,
  val bitsPerRgbValue: Int,
  val colorMapEntries: Int,
  val redMask: Int,
  val greenMask: Int,
  val blueMask: Int
) {
  def toByteString(implicit endian: java.nio.ByteOrder): ByteString = {
    import ExtendedByteStringBuilder._
    val builder = ByteString.newBuilder
    builder.putInt(visualId)
    builder.putByte(colorClass.id toByte)
    builder.putByte(bitsPerRgbValue toByte)
    builder.putShort(colorMapEntries toShort)
    builder.putInt(redMask)
    builder.putInt(greenMask)
    builder.putInt(blueMask)
    builder.fill(4)
    builder result
  }
}
