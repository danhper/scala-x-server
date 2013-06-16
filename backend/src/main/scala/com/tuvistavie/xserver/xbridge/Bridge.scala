package com.tuvistavie.xserver.backend.xbridge

import akka.actor.{ Actor, ActorRef, ActorSystem, ActorLogging, Props }

import com.tuvistavie.xserver.backend.util.Config

class Bridge extends Actor with ActorLogging {
  def receive = {
    case foo =>
  }
}

object Bridge {
  val system = ActorSystem("XBridgeClient")
  val ref = system.actorOf(Props[Bridge], "bridge")

  private val server = system.actorFor(Config.getString("bridge.remote.path"))

  def register = server ! ref
}
