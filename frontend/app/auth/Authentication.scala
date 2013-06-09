package com.tuvistavie.xserver.frontend.auth

trait PasswordAuthentication {
  def authenticate(username: String, password: String): Boolean
}

trait TokenAuthentication {
  def authenticate(token: String): Boolean
}

