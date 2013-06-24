package com.tuvistavie.xserver.bridge

import scala.concurrent.Future
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.util.Timeout
import play.api.Logger
import play.api.libs.json.{ JsValue, JsObject, JsString }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.{ Iteratee, Done, Input, Enumerator }

import com.tuvistavie.xserver.frontend.auth.User


object BridgeManager {
  private var bridges = Map[Int, ActorRef]()
  private implicit val timeout = Timeout(1 second)

  private val system = ActorSystem("XBridgeServer", ConfigFactory.load.getConfig("xbridge-server"))

  def create(user: User): Boolean = {
    val actorName = s"bridgeServer-${user.id}"
    Logger.debug(s"starting actor ${actorName}")
    val actor = system.actorOf(Props(new Bridge(user)), actorName)
    bridges += (user.id -> actor)
    true
  }

  def connect(userOpt: Option[User]) = userOpt flatMap { u => bridges get(u.id) } match {
    case Some(bridge) => {
      (bridge ? Connect).map {
        case Connected(enumerator) => {
          val iteratee = Iteratee.foreach[JsValue] { event =>
            bridge ! JsonMessage(event)
          }.mapDone { _ =>
            bridge ! PoisonPill
          }
          (iteratee, enumerator)
        }
      }
    }
    case None => {
      val iteratee = Done[JsValue, Unit]((),Input.EOF)
      val enumerator = Enumerator[JsValue](JsObject(Seq("error" -> JsString("foobar")))) >>> Enumerator.eof
      Future(iteratee,enumerator)
    }
  }
}
