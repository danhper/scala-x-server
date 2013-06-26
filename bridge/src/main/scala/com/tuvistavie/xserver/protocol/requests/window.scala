package com.tuvistavie.xserver.protocol.request

import akka.actor.IO
import com.typesafe.scalalogging.slf4j.Logging

import com.tuvistavie.xserver.backend.model.InputClass

object WindowRequestHelper extends ValueGenerator {
  val values = List(
    ValueInfo(0x00000001, "backgroundPixmap", 4),
    ValueInfo(0x00000002, "backgroundPixel", 4),
    ValueInfo(0x00000004, "borderPixmap", 4),
    ValueInfo(0x00000008, "borderPixel", 4),
    ValueInfo(0x00000010, "bitGravity", 1),
    ValueInfo(0x00000020, "winGravity", 1),
    ValueInfo(0x00000040, "backingStore", 1),
    ValueInfo(0x00000080, "backingPlanes", 4),
    ValueInfo(0x00000100, "backingPixel", 4),
    ValueInfo(0x00000200, "overrideRedirect", 1),
    ValueInfo(0x00000400, "saveUnder", 1),
    ValueInfo(0x00000800, "eventMask", 4),
    ValueInfo(0x00001000, "doNotPropagateMask", 4),
    ValueInfo(0x00002000, "colormap", 4),
    ValueInfo(0x00004000, "cursor", 4)
  )
}

case class CreateWindowRequest (
  id: Int,
  parent: Int,
  depth: Int,
  x: Int,
  y: Int,
  width: Int,
  height: Int,
  borderWidth: Int,
  inputClass: InputClass,
  visualId: Int,
  values: Map[String, Int]
) extends Request
with NeedsTransfer {
  val opCode = 1
}

object CreateWindowRequest extends RequestGenerator with Logging {
  override def parseRequest(length: Int, depth: Int)(implicit endian: java.nio.ByteOrder) = {
    for {
      request <- IO.take(length)
    } yield {
      val iterator = request.iterator
      val wid = iterator getInt
      val parent = iterator getInt
      val (x, y) = (iterator getShort, iterator getShort)
      val (width, height) = (iterator getShort, iterator getShort)
      val borderWidth = iterator getShort
      val inputClass = InputClass.fromInt(iterator getShort)
      val visualId = iterator getInt
      val bitMask = iterator getInt
      val values = WindowRequestHelper.getValues(bitMask, iterator)
      CreateWindowRequest(wid, parent, depth, x, y, width, height,
        borderWidth, inputClass, visualId, values)
    }
  }
}
