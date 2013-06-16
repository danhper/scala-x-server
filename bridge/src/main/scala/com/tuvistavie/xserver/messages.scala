package com.tuvistavie.xserver.bridge.messages

import akka.actor.ActorRef

sealed trait Message
case class Register(actor: ActorRef) extends Message
