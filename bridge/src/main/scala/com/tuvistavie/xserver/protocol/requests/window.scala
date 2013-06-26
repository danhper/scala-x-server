package com.tuvistavie.xserver.protocol.request

import com.tuvistavie.xserver.backend.model.InputClass


case class CreateWindowRequest (
  id: Int,
  parent: Int,
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


object WindowRequestHelper {

}
