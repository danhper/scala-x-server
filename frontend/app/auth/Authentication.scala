package com.tuvistavie.xserver.frontend.auth

import play.api.Play

import scala.sys.process.Process

trait PasswordAuthentication {
  def authenticate(username: String, password: String): Option[User]
}

trait TokenAuthentication {
  def authenticate(token: String): Option[User]
}

class DummyPasswordAuthentication extends PasswordAuthentication {
  override def authenticate(username: String, password: String) = {
    Some(UserManager.current.createUser(username, password))
  }
}

class UnixAuthentication extends PasswordAuthentication {
  override def authenticate(username: String, password: String): Option[User] = {
    val authPath = Play.current.configuration.getString("paths.nix-password-checker").get
    val pb = Process(authPath, Seq(username, password))
    val exitCode: Int = pb.!
    if(exitCode == 0) Some(UserManager.current.createUser(username, password))
    else None
  }
}

class SimpleTokenAuthentication extends TokenAuthentication {
  override def authenticate(token: String): Option[User] = {
    UserManager.current.findUserByToken(token)
  }
}
