package com.tuvistavie.xserver.frontend.auth

import play.api.Play

import scala.sys.process.Process

trait UnixAuthentication extends PasswordAuthentication {
  def authenticate(username: String, password: String): Boolean = {
    val authPath = Play.current.configuration.getString("misc.nix-password-checker-path").get
    val pb = Process(authPath, Seq(username, password))
    val exitCode: Int = pb.!
    exitCode == 0
  }
}
