package com.tuvistavie.xserver.bridge

import scala.sys.addShutdownHook
import scala.sys.process.Process
import akka.actor.{ Actor, ActorRef, ActorLogging }
import play.api.Play
import play.api.libs.json.JsValue
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Enumerator

import com.tuvistavie.xserver.frontend.auth.UserManager
import com.tuvistavie.xserver.bridge.messages.Register
import com.tuvistavie.xserver.frontend.util.Config

class Bridge (
  val id: Int,
  username: String
) extends Actor with ActorLogging {
  private var initialized = false
  private val shutdownHook = addShutdownHook(stopBridge)
  private var remoteBridge: Option[ActorRef] = None

  private val binPath = Play.current.configuration.getString("paths.backend").get
  private val clientBasePort = Play.current.configuration.getInt("xbridge-server.client.base-port").get

  val wsEnumerator = Concurrent.unicast[JsValue]{ c =>
    log.debug(s"created enumerator ${self.toString}")
  }

  override def preStart() {
    log.debug("starting actor with path {}", self.path.toString)
    val port = s"wrapper.java.additional.2=-Dbridge.akka.remote.netty.port=${clientBasePort + id}"
    val args = List("start", port, "--", "-n", id.toString)
    val process = Process(binPath :: args, None, "RUN_USER" -> username)
    process.run()
  }

  override def postStop() {
    stopBridge()
    shutdownHook.remove()
    UserManager.current.removeUser(id)
  }

  private def stopBridge() {
    Process(Seq(binPath, "stop"), None, "RUN_USER" -> username).!
  }

  def receive = {
    case Register(actor) => {
      initialized = true
      remoteBridge = Some(actor)
      log.debug("registered actor {}", actor.toString)
    }
    case _ if !initialized => {
      throw new RuntimeException("message received before registration")
    }
  }
}

