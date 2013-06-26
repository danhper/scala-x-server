package com.tuvistavie.xserver.protocol.request

import scala.collection.mutable.MapBuilder
import com.typesafe.scalalogging.slf4j.Logging
import akka.util.ByteIterator
import akka.actor.IO

import com.tuvistavie.xserver.backend.util.ExtendedByteIterator

object GCRequestHelper extends ValueGenerator {
  val values = List(
    ValueInfo(0x00000001, "function", 1),
    ValueInfo(0x00000002, "planeMask", 4),
    ValueInfo(0x00000004, "foreground", 4, true),
    ValueInfo(0x00000008, "background", 4, true),
    ValueInfo(0x00000010, "lineWidth", 2),
    ValueInfo(0x00000020, "lineStyle", 1),
    ValueInfo(0x00000040, "capStyle", 1),
    ValueInfo(0x00000080, "joinStyle", 1),
    ValueInfo(0x00000100, "fillStyle", 1),
    ValueInfo(0x00000200, "fillRule", 1),
    ValueInfo(0x00000400, "tile", 4),
    ValueInfo(0x00000800, "stipple", 4),
    ValueInfo(0x00001000, "tileStippleXOrigin", 2),
    ValueInfo(0x00002000, "tileStippleYOrigin", 2),
    ValueInfo(0x00004000, "font", 4),
    ValueInfo(0x00008000, "subwindowMode", 1),
    ValueInfo(0x00010000, "graphicsExposures", 1),
    ValueInfo(0x00020000, "clipXOrigin", 2),
    ValueInfo(0x00040000, "clipYOrigin", 2),
    ValueInfo(0x00080000, "clipMask", 4),
    ValueInfo(0x00100000, "dashOffset", 2),
    ValueInfo(0x00200000, "dashes", 1),
    ValueInfo(0x00400000, "arcMode", 2)
  )
}

case class CreateGCRequest (
  cid: Int,
  drawable: Int,
  values: Map[String, Int]
) extends Request
with NeedsTransfer {
  val opCode = 55
}

object CreateGCRequest extends RequestGenerator with Logging {
  override def parseRequest(length: Int, data: Int)(implicit endian: java.nio.ByteOrder) = {
    for {
      request <- IO.take(length)
    } yield {
      val iterator = request.iterator
      val contextId = iterator.getInt
      val drawable = iterator.getInt
      val bitMask = iterator.getInt
      val values = GCRequestHelper.getValues(bitMask, iterator)
      CreateGCRequest(contextId, drawable, values)
    }
  }
}
