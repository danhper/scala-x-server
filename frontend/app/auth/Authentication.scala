package com.tuvistavie.xserver.frontend.auth

import play.api.Play

import scala.sys.process.Process


trait PasswordAuthentication {
  def authenticate(username: String, password: String): Option[User]
}

trait TokenAuthentication {
  def authenticate(token: String): Option[User]
}

trait DummyPasswordAuthentication extends PasswordAuthentication {
  override def authenticate(username: String, password: String) = {
    Some(UserManager.current.createUser(username, password))
  }
}

trait UnixAuthentication extends PasswordAuthentication {
  override def authenticate(username: String, password: String): Option[User] = {
    val authPath = Play.current.configuration.getString("paths.nix-password-checker").get
    val pb = Process(authPath, Seq(username, password))
    val exitCode: Int = pb.!
    if(exitCode == 0) Some(UserManager.current.createUser(username, password))
    else None
  }
}

trait SimpleTokenAuthentication extends TokenAuthentication {
  override def authenticate(token: String): Option[User] = {
    UserManager.current.findUserByToken(token)
  }
}

trait LoginManager {
  def authenticate(token: String): Option[User]
  def authenticate(username: String, password: String): Option[User]
}

object DummyLoginManager extends LoginManager with DummyPasswordAuthentication with SimpleTokenAuthentication

object NixLoginManager extends LoginManager with UnixAuthentication with SimpleTokenAuthentication
