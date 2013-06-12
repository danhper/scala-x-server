package com.tuvistavie.xserver.frontend.xbridge

import com.tuvistavie.xserver.frontend.auth.User

import akka.actor.Actor

object BridgeManager {
  private var bridges = Map[Int, Bridge]()

  def create(user: User): Bridge = {
    val bridge = new Bridge(user.name)
    bridges += (user.id -> bridge)
    bridge.launch(user.id + 2)
    bridge
  }
}

class BridgeManager (
) extends Actor {
  def receive: Actor.Receive = {
    case foo =>
  }

}
