package com.tuvistavie.xserver.protocol.reply

import com.tuvistavie.xserver.io._
import com.tuvistavie.xserver.protocol.types._

abstract class Reply(
  val data: Option[Card8],
  val sequenceNumber: Card16,
  val length: Card32
) {
  def write(stream: BinaryOutputStream) = {
    stream.writeByte(1)
    data match {
      case Some(n) => stream.writeByte(n)
      case _ => stream.writeByte(0)
    }
    stream.writeUInt16(sequenceNumber)
    stream.writeUInt32(length)
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
    super.write(stream)
    List[Value](visual, className, bitGravity, windowGravity,
      backingPlanes, backingPixels, saveUnder, mapIsInstalled,
      mapState, overrideRedirect, colorMap, allEventMasks,
      yourEventMask, doNotPropagateMask).foreach { stream.writeValue _ }
    stream.fill(2)
  }

}
