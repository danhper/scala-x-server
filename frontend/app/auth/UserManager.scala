package com.tuvistavie.xserver.frontend.auth

import play.api.Play

import java.security.MessageDigest
import sun.misc.BASE64Encoder

case class User(id: Int, token: String)

object UserManager {
  val current = new UserManager {
    val hasher = new Sha512Hasher
  }

}

trait UserManager {
  protected val hasher: Hasher

  private val maxUsers = Play.current.configuration.getInt("application.max_connections").get
  private var users = Map[String, User]()
  private val availableIds: Array[Boolean] = Array.fill(maxUsers)(true)

  def canCreateUser = availableIds.exists(_ == true)
  def findUserByToken(token: String) = users.get(token)

  def createUser(username: String, password: String): User = {
    val id = availableIds.indexOf(true)
    availableIds(id) = false
    val token = hasher.hash(username + password)
    val user = User(id, token)
    users += (token -> user)
    user
  }
}

trait Hasher {
  val hashAlgorithm: String
  def hash(str: String): String = {
    val md = MessageDigest.getInstance(hashAlgorithm)
    val encoder = new BASE64Encoder()
    encoder.encode(md.digest(str.getBytes))
  }
}

class Sha512Hasher extends Hasher {
  override val hashAlgorithm = "SHA-512"
}
