package com.tuvistavie.xserver.protocol.requests;

import com.tuvistavie.xserver.protocol._

case class CreateWindow(val depth: Int8) extends AbstractRequest(1) {

}
