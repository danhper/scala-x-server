package com.tuvistavie.xserver.frontend.auth

case class User (
  name: String,
  id: Int,
  token: String,
  properties: UserProperties = UserProperties.default
)

object User {
  def apply(name: String): User = User(name, -1, "")
}

case class UserProperties(
  windowWidth: Int,
  windowHeight: Int
)

object UserProperties {
  val default = UserProperties(800, 600)
}
