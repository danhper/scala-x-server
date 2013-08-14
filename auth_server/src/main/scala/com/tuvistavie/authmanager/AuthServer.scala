package com.tuvistavie.authmanager

import akka.actor.{ Actor, ActorSystem, ActorLogging, Props }
import scala.sys.process.Process
import com.typesafe.config.ConfigFactory

case class Authenticate (
  id: Int,
  username: String,
  password: String
)

case object Success
case object Failure

class AuthManager extends Actor with ActorLogging {
  private val clientBasePort = AuthManager.config.getInt("xbridge-server.client.base-port")
  private val binPath = AuthManager.config.getString("paths.backend")

  def receive = {
    case Authenticate(id, username, password) => {
      log.debug("trying to authenticate {}", username)
      val authenticated = checkAuthentication(username, password)
      if(authenticated) {
        launchBackend(id, username)
        sender ! Success
      } else sender ! Failure
    }
    case _ => sender ! Failure
  }

  private def checkAuthentication(username: String, password: String): Boolean = {
    val pb = Process(AuthManager.authPath, Seq(username, password))
    val exitCode: Int = pb.!
    log.debug("auth exitcode: {}", exitCode)
    exitCode == 0
  }

  private def launchBackend(id: Int, username: String) {
    log.debug("starting backend app")
    val port = s"wrapper.java.additional.2=-Dbridge.akka.remote.netty.port=${clientBasePort + id}"
    // FIXME: use data received from browser instead of hard coded dimensions
    val args = List("start", port, "--", "-n", id.toString, "-w", 800.toString, "-h", 600.toString)
    val process = Process(binPath :: args, None, "RUN_AS_USER" -> username)
    process.run()
  }
}

object AuthManager {
  val config = ConfigFactory.load()
  val system = ActorSystem("AuthServer", ConfigFactory.load.getConfig("auth-server"))
  val ref = system.actorOf(Props[AuthManager], "authManager")
  val authPath = config.getString("paths.nix-password-checker")
}
