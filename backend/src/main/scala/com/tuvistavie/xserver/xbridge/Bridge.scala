package com.tuvistavie.xserver.bridge

import akka.actor.{ Actor, ActorRef, ActorSystem, ActorLogging, Props }

import com.tuvistavie.xserver.backend.util.Config
import messages.Register

class BridgeClient extends Actor with ActorLogging {
  def receive = {
    case foo =>
  }
}

object BridgeClient {
  val system = ActorSystem("XBridgeClient")
  val ref = system.actorOf(Props[BridgeClient], "bridge")

  private val server = system.actorFor(Config.getString("bridge.server.path"))

  def register() = server ! Register(ref)
}
