package com.tuvistavie.xserver.frontend.auth

import play.api.{ Play, Mode }

import scala.util.Random
import java.security.MessageDigest
import sun.misc.BASE64Encoder

case class User(id: Int, name: String, token: String)

object UserManager {
  val current = new UserManager {
    import Play.current

    val hasher = Play.mode match {
      case Mode.Prod => new Sha512Hasher
      case _ => new NoOpHasher
    }

    val tokenGenerator = Play.mode match {
      case Mode.Prod => new RandomTokenGenerator
      case _ => new SimpleTokenGenerator
    }
  }
}

trait UserManager {
  protected val hasher: Hasher
  protected val tokenGenerator: TokenGenerator

  private val maxUsers = Play.current.configuration.getInt("application.max_connections").get
  private var users = Map[String, User]()
  private val availableIds: Array[Boolean] = Array.fill(maxUsers)(true)

  def canCreateUser = availableIds.exists(_ == true)
  def findUserByToken(token: String) = users.get(token)

  def createUser(username: String, password: String): User = {
    val id = availableIds.indexOf(true)
    availableIds(id) = false
    val token = hasher.hash(tokenGenerator.generateToken(username, password))
    val user = User(id, username, token)
    users += (token -> user)
    user
  }
}

trait TokenGenerator {
  def generateToken(username: String, password: String): String
}

class SimpleTokenGenerator extends TokenGenerator {
  override def generateToken(username: String, password: String): String = {
    username + password
  }
}

class RandomTokenGenerator extends TokenGenerator {
  override def generateToken(username: String, password: String): String = {
    val randomString = Random.nextString(Play.current.configuration.getInt("auth.random-string-length").get)
    val timeStamp = (System.currentTimeMillis.toString)
    username + randomString + timeStamp + password
  }
}

trait Hasher {
  def hash(str: String): String
}

class NoOpHasher extends Hasher {
  override def hash(str: String) = str
}

trait DigestHasher extends Hasher {
  val hashAlgorithm: String
  override def hash(str: String): String = {
    val md = MessageDigest.getInstance(hashAlgorithm)
    val encoder = new BASE64Encoder()
    encoder.encode(md.digest(str.getBytes))
  }
}

class Sha512Hasher extends DigestHasher {
  override val hashAlgorithm = "SHA-512"
}
