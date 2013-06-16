package com.tuvistavie.xserver.bridge

import scala.sys.addShutdownHook
import scala.sys.process.Process
import akka.actor.{ Actor, ActorRef, ActorLogging }
import play.api.Play

import com.tuvistavie.xserver.bridge.messages.Register

class Bridge (
  val id: Int,
  username: String
) extends Actor with ActorLogging {
  private var initialized = false
  private val shutdownHook = addShutdownHook(stopBridge)
  private var remoteBridge: Option[ActorRef] = None

  private val binPath = Play.current.configuration.getString("paths.backend").get

  override def preStart() {
    val args = List("start", "--", "-n", id.toString)
    val process = Process(binPath :: args, None, "RUN_USER" -> username)
    process.run()
  }

  override def postStop() {
    stopBridge()
    shutdownHook.remove()
  }

  private def stopBridge() {
    Process(Seq(binPath, "stop"), None, "RUN_USER" -> username).!
  }

  def receive = {
    case Register(actor) => {
      initialized = true
      remoteBridge = Some(actor)
    }
    case _ if !initialized => {
      throw new RuntimeException("message received before registration")
    }
  }
}

