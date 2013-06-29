package com.tuvistavie.xserver.bridge.messages

import akka.actor.ActorRef
import com.tuvistavie.xserver.protocol.request.Request

sealed trait Message
case class Register(actor: ActorRef) extends Message
case class AddClient(clientId: Int) extends Message
case class RemoveClient(clientId: Int) extends Message
case class RequestMessage(clientId: Int, request: Request) extends Message
