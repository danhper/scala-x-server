package com.tuvistavie.xserver.bridge

import akka.actor.{ Actor, ActorRef, ActorSystem, ActorLogging, Props }

import com.tuvistavie.xserver.backend.util.{ Config, RuntimeConfig }
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.Logging

import messages.Register

trait BridgeClientLike {
  def register(): Unit
  def !(msg: Any): Unit
}

class BridgeClient extends Actor with ActorLogging {
  def receive = {
    case foo =>
  }
}

object BridgeClient extends Logging with BridgeClientLike {
  val system = ActorSystem("XBridgeClient", ConfigFactory.load.getConfig("bridge"))
  val ref = system.actorOf(Props[BridgeClient], "bridge-" + RuntimeConfig.displayNumber)

  private val serverPath = Config.getString("bridge.server.path").format(RuntimeConfig.displayNumber)
  lazy val server = system.actorFor(serverPath)

  override def register() {
    logger.debug(s"registering to actor with path ${serverPath}")
    server ! Register(ref)
  }

  override def !(msg: Any) {
    server ! msg
  }
}

object DummyBridgeClient extends BridgeClientLike {
  override def register() { }

  override def !(msg: Any) { }
}
