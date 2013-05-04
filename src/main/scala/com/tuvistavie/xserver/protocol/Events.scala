package com.tuvistavie.xserver.protocol.events

import com.tuvistavie.xserver.protocol.types._

import com.tuvistavie.xserver.io._

abstract class Event(val code: Int8)

case class KeyPress (
  val keycode: Keycode,
  val sequenceNumber: Int16,
  val time: Timestamp,
  val root: Window,
  val event: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val eventX: Int16,
  val eventY: Int16,
  val state: Int16,
  val sameScreen: Bool
) extends Event(2)

object KeyPress {
  def apply(stream: BinaryInputStream, keycode: Keycode) = {
    val keycode = stream.readUInt8()
    val sequenceNumber = stream.readInt16()
    val time = stream.readUInt32()
    val root = stream.readUInt32()
    val event = stream.readUInt32()
    val child = stream.readUInt32()
    val rootX = stream.readInt16()
    val rootY = stream.readInt16()
    val eventX = stream.readInt16()
    val eventY = stream.readInt16()
    val state = stream.readInt16()
    val sameScreen = stream.readInt8()
    stream.skip(1)
    new KeyPress (keycode, sequenceNumber, time, root, event,
      child, rootX, rootY, eventX, eventY, state, sameScreen)
  }
}
