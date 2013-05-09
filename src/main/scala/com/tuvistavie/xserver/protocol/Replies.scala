package com.tuvistavie.xserver.protocol.reply

import com.tuvistavie.xserver.io._
import com.tuvistavie.xserver.protocol.types._

abstract class Reply(
  val data: Card8,
  val sequenceNumber: Card16,
  val length: Card32
) {

}

case class GetWindowAttributes (

)
