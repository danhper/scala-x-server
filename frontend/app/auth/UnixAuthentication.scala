package com.tuvistavie.xserver.frontend.auth

import play.api.Play

import scala.sys.process.Process

trait UnixAuthentication extends PasswordAuthentication {
  override def authenticate(username: String, password: String): Option[User] = {
    val authPath = Play.current.configuration.getString("paths.nix-password-checker").get
    val pb = Process(authPath, Seq(username, password))
    val exitCode: Int = pb.!
    if(exitCode == 0) Some(UserManager.current.createUser(username, password))
    else None
  }
}
