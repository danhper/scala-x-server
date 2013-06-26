package com.tuvistavie.xserver.bridge

import scala.sys.addShutdownHook
import scala.sys.process.Process
import akka.actor.{ Actor, ActorRef, ActorLogging }
import play.api.Play
import play.api.libs.json.{ Json, JsValue, JsObject, JsString }
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Enumerator

import com.tuvistavie.xserver.frontend.auth.{ UserManager, User }
import com.tuvistavie.xserver.bridge.messages.{ Register, RequestMessage }
import com.tuvistavie.xserver.protocol.request.CreateGCRequest
import com.tuvistavie.xserver.frontend.util.Config

import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import org.json4s.JsonDSL._
import org.json4s.{ NoTypeHints, Extraction, JValue }

case object Connect
case class Connected(enumerator: Enumerator[JValue])
case class CannotConnect(error: String)
case class JsonMessage(message: JValue)


class Bridge (
  user: User
) extends Actor with ActorLogging {

  private var initialized = false
  private val shutdownHook = addShutdownHook(stopBridge)
  private var remoteBridge: Option[ActorRef] = None

  private val binPath = Play.current.configuration.getString("paths.backend").get
  private val clientBasePort = Play.current.configuration.getInt("xbridge-server.client.base-port").get

  val wsEnumerator = Concurrent.unicast[JValue]{ c =>
    log.debug(s"created enumerator ${self.toString} with channel ${c.toString}")
    channel = Some(c)
  }
  private var channel: Option[Concurrent.Channel[JValue]] = None
  private lazy val wsChannel: Concurrent.Channel[JValue] = channel.get

  implicit val formats = Serialization.formats(NoTypeHints)

  override def preStart() {
    log.debug("starting actor with path {}", self.path.toString)
    val port = s"wrapper.java.additional.2=-Dbridge.akka.remote.netty.port=${clientBasePort + user.id}"
    val args = List("start", port, "--", "-n", user.id.toString, "-w", user.properties.windowWidth.toString, "-h", user.properties.windowHeight.toString)
    val process = Process(binPath :: args, None, "RUN_AS_USER" -> user.name)
    process.run()
  }

  override def postStop() {
    stopBridge()
    shutdownHook.remove()
    UserManager.current.removeUser(user.id)
  }

  private def stopBridge() {
    Process(Seq(binPath, "stop"), None, "RUN_USER" -> user.name).!
  }

  def receive = {
    case Register(actor) => {
      initialized = true
      remoteBridge = Some(actor)
      log.debug("registered actor {}", actor.toString)
    }
    case RequestMessage(request) => {
      log.debug("sending request {} to browser", request)
      val jsonRequest = Extraction.decompose(Map(
        "opCode"  -> request.opCode,
        "type"    -> request.getClass.getSimpleName,
        "request" -> request
      ))
      wsChannel.push(jsonRequest)
    }
    case Connect => {
      sender ! Connected(wsEnumerator)
      log.debug("websocket connection accepted")
    }
    case JsonMessage(message) => {
      log.debug("received message " + message.toString)
      val json = ("maybe" -> "it works")
      wsChannel.push(json)
    }
    case unknownMessage => {
      log.debug("received unkonwn messaged {}", unknownMessage)
    }
  }
}

