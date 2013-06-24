package com.tuvistavie.xserver.frontend.auth

import play.api.Play
import play.api.mvc.{ RequestHeader, AnyContent }
import scala.sys.process.Process

import com.tuvistavie.xserver.frontend.util.Config

trait PasswordAuthentication {
  def authenticate(username: String, password: String): Option[User]
}

trait TokenAuthentication {
  def authenticate(token: String): Option[User]
}

trait DummyPasswordAuthentication extends PasswordAuthentication {
  override def authenticate(username: String, password: String) = {
    Some(User(username))
  }
}

trait UnixAuthentication extends PasswordAuthentication {
  override def authenticate(username: String, password: String): Option[User] = {
    val authPath = Config.getString("paths.nix-password-checker")
    val pb = Process(authPath, Seq(username, password))
    val exitCode: Int = pb.!
    if(exitCode == 0) Some(User(username))
    else None
  }
}

trait SimpleTokenAuthentication extends TokenAuthentication {
  override def authenticate(token: String): Option[User] = {
    UserManager.current findUserByToken(token)
  }
}

abstract class LoginManager extends PasswordAuthentication with TokenAuthentication {

  def login(implicit request: RequestHeader): Option[User] = {
    request.session get(Config.getString("auth.token-name")) flatMap { authenticate _ }
  }
}

object DummyLoginManager extends LoginManager with DummyPasswordAuthentication with SimpleTokenAuthentication

object NixLoginManager extends LoginManager with UnixAuthentication with SimpleTokenAuthentication
