package com.tuvistavie.xserver.bridge.messages

import akka.actor.ActorRef

sealed trait Message
case class Register(id: Int, actor: ActorRef) extends Message
