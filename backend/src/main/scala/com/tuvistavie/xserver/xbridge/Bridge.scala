package com.tuvistavie.xserver.bridge

import akka.actor.{ Actor, ActorRef, ActorSystem, ActorLogging, Props }

import com.tuvistavie.xserver.backend.util.{ Config, RuntimeConfig }
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.Logging

import messages.Register

class BridgeClient extends Actor with ActorLogging {
  def receive = {
    case foo =>
  }
}

object BridgeClient extends Logging {
  val system = ActorSystem("XBridgeClient", ConfigFactory.load.getConfig("bridge"))
  val ref = system.actorOf(Props[BridgeClient], "bridge-" + RuntimeConfig.displayNumber)

  private val serverPath = Config.getString("bridge.server.path").format(RuntimeConfig.displayNumber)
  private val server = system.actorFor(serverPath)

  def register() = {
    logger.debug(s"registering to actor with path ${serverPath}")
    server ! Register(ref)
  }
}
