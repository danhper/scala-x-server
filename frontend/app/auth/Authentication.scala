package com.tuvistavie.xserver.frontend.auth

trait PasswordAuthentication {
  def authenticate(username: String, password: String): Option[User]
}

trait TokenAuthentication {
  def authenticate(token: String): Option[User]
}
