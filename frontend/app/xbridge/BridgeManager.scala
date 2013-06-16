package com.tuvistavie.xserver.bridge

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import com.typesafe.config.ConfigFactory

import com.tuvistavie.xserver.frontend.auth.User

object BridgeManager {
  private var bridges = Map[Int, ActorRef]()
  val system = ActorSystem("XBridgeServer", ConfigFactory.load.getConfig("xbridge-server"))

  def create(user: User): Boolean = {
    val actor = system.actorOf(Props(new Bridge(user.id, user.name)), s"bridgeServer-${user.id}")
    bridges += (user.id -> actor)
    true
  }
}
