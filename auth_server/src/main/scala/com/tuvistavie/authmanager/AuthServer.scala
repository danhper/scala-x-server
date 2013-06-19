package com.tuvistavie.authmanager

import akka.actor.{ Actor, ActorSystem, ActorLogging, Props }
import scala.sys.process.Process
import com.typesafe.config.ConfigFactory

case class Authenticate (
  username: String,
  password: String
)

case object Success
case object Failure

class AuthManager extends Actor with ActorLogging {
  def receive = {
    case Authenticate(username, password) => {
      log.debug("trying to authenticate {}", username)
      val authenticated = checkAuthentication(username, password)
      if(authenticated) {
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
}

object AuthManager {
  val config = ConfigFactory.load()
  val system = ActorSystem("AuthServer", ConfigFactory.load.getConfig("auth-server"))
  val ref = system.actorOf(Props[AuthManager], "authManager")
  val authPath = config.getString("paths.nix-password-checker")
}
