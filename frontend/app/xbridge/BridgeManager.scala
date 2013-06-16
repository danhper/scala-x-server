package com.tuvistavie.xserver.bridge

import play.api.Logger
import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import play.api.libs.iteratee.Enumerator
import play.api.libs.json.JsValue
import play.api.libs.concurrent.Execution.Implicits._


import com.tuvistavie.xserver.frontend.auth.User

case class Connect(user: Option[User])
case class Connected(bridge: Bridge)
case class CannotConnect(error: String)


object BridgeManager {
  private var bridges = Map[Int, ActorRef]()
  private implicit val timeout = Timeout(1 second)

  private val system = ActorSystem("XBridgeServer", ConfigFactory.load.getConfig("xbridge-server"))

  private val default = system.actorOf(Props[BridgeManager])

  def create(user: User): Boolean = {
    val actorName = s"bridgeServer-${user.id}"
    Logger.debug(s"starting actor ${actorName}")
    val actor = system.actorOf(Props(new Bridge(user.id, user.name)), actorName)
    bridges += (user.id -> actor)
    true
  }

  def connect(user: Option[User]) = (default ? Connect(user)) map {
    case Connected(bridge) => {
    }
  }
}

class BridgeManager extends Actor {

  def receive = {
    case Connect(userOpt) => {
      userOpt flatMap { u => BridgeManager.bridges.get(u.id) } match {
        case None =>
      }
    }
  }

}
