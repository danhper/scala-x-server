package com.tuvistavie.authmanager

import akka.actor.Actor

case class Authenticate (
  username: String,
  password: String,
  block: (_ => Unit)
)

class AuthManager extends Actor {
  def receive = {
    case Authenticate(u, password, block) => {

    }
  }

  private def checkAuthentication: Boolean = {
    false
  }
}
