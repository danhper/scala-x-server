package com.tuvistavie.authmanager

import akka.actor.{ Actor, ActorSystem, Props }
import scala.sys.process.Process
import com.typesafe.config.ConfigFactory

case class Authenticate (
  username: String,
  password: String,
  block: (() => Unit)
)

case object Success
case object Failure

class AuthManager extends Actor {
  def receive = {
    case Authenticate(username, password, block) => {
      val authenticated = checkAuthentication(username, password)
      if(authenticated) {
        block()
        sender ! Success
      } else sender ! Failure
    }
  }

  private def checkAuthentication(username: String, password: String): Boolean = {
    val pb = Process(AuthManager.authPath, Seq(username, password))
    val exitCode: Int = pb.!
    exitCode == 0
  }
}

object AuthManager {
  val config = ConfigFactory.load()
  val system = ActorSystem("AuthServer")
  val ref = system.actorOf(Props[AuthManager], "authManager")
  val authPath = config.getString("paths.nix-password-checker")
}
