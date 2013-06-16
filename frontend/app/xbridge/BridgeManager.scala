package com.tuvistavie.xserver.bridge

import play.api.Logger
import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import com.typesafe.config.ConfigFactory

import com.tuvistavie.xserver.frontend.auth.User

object BridgeManager {
  private var bridges = Map[Int, ActorRef]()
  val system = ActorSystem("XBridgeServer", ConfigFactory.load.getConfig("xbridge-server"))

  def create(user: User): Boolean = {
    val actorName = s"bridgeServer-${user.id}"
    Logger.debug(s"starting actor ${actorName}")
    val actor = system.actorOf(Props(new Bridge(user.id, user.name)), actorName)
    bridges += (user.id -> actor)
    true
  }
}
