package com.tuvistavie.xserver.protocol.request

import scala.collection.mutable.MapBuilder
import com.typesafe.scalalogging.slf4j.Logging
import akka.util.ByteIterator
import akka.actor.IO

import com.tuvistavie.xserver.backend.util.ExtendedByteIterator


case class CreateGCRequest (
  cid: Int,
  drawable: Int,
  values: Map[String, Int]
) extends Request(55)

object GC {
  import ExtendedByteIterator._

  def getValues(mask: Int, iterator: ByteIterator)(implicit endian: java.nio.ByteOrder) = {
    val mapBuilder = new MapBuilder[String, Int, Map[String, Int]](Map.empty)
    if((mask & 0x00000001) != 0) mapBuilder += ("function" -> iterator.getPaddedByte)
    if((mask & 0x00000002) != 0) mapBuilder += ("planeMask" -> iterator.getInt)
    if((mask & 0x00000004) != 0) mapBuilder += ("foreground" -> iterator.getInt)
    if((mask & 0x00000008) != 0) mapBuilder += ("background" -> iterator.getInt)
    if((mask & 0x00000010) != 0) mapBuilder += ("lineWidth" -> iterator.getPaddedShort)
    if((mask & 0x00000020) != 0) mapBuilder += ("lineStyle" -> iterator.getPaddedByte)
    if((mask & 0x00000040) != 0) mapBuilder += ("capStyle" -> iterator.getPaddedByte)
    if((mask & 0x00000080) != 0) mapBuilder += ("joinStyle" -> iterator.getPaddedByte)
    if((mask & 0x00000100) != 0) mapBuilder += ("fillStyle" -> iterator.getPaddedByte)
    if((mask & 0x00000200) != 0) mapBuilder += ("fillRule" -> iterator.getPaddedByte)
    if((mask & 0x00000400) != 0) mapBuilder += ("tile" -> iterator.getInt)
    if((mask & 0x00000800) != 0) mapBuilder += ("stipple" -> iterator.getInt)
    if((mask & 0x00001000) != 0) mapBuilder += ("tileStippleXOrigin" -> iterator.getPaddedShort)
    if((mask & 0x00002000) != 0) mapBuilder += ("tileStippleYOrigin" -> iterator.getPaddedShort)
    if((mask & 0x00004000) != 0) mapBuilder += ("font" -> iterator.getInt)
    if((mask & 0x00008000) != 0) mapBuilder += ("subwindowMode" -> iterator.getPaddedByte)
    if((mask & 0x00010000) != 0) mapBuilder += ("graphicsExposures" -> iterator.getPaddedByte)
    if((mask & 0x00020000) != 0) mapBuilder += ("clipXOrigin" -> iterator.getPaddedShort)
    if((mask & 0x00040000) != 0) mapBuilder += ("clipYOrigin" -> iterator.getPaddedShort)
    if((mask & 0x00080000) != 0) mapBuilder += ("clipMask" -> iterator.getInt)
    if((mask & 0x00100000) != 0) mapBuilder += ("dashOffset" -> iterator.getPaddedShort)
    if((mask & 0x00200000) != 0) mapBuilder += ("dashes" -> iterator.getPaddedByte)
    if((mask & 0x00400000) != 0) mapBuilder += ("arcMode" -> iterator.getPaddedShort)
    mapBuilder result
  }
}

object CreateGCRequest extends RequestGenerator with Logging {
  override def parseRequest(length: Int, date: Int)(implicit endian: java.nio.ByteOrder) = {
    for {
      request <- IO.take(length)
    } yield {
      val iterator = request.iterator
      val contextId = iterator.getInt
      val drawable = iterator.getInt
      val bitMask = iterator.getInt
      val values = GC.getValues(bitMask, iterator)
      CreateGCRequest(contextId, drawable, values)
    }
  }
}
