package com.tuvistavie.xserver.frontend.auth

import play.api.{ Play, Mode }

import scala.util.Random
import java.security.MessageDigest
import sun.misc.BASE64Encoder

import com.tuvistavie.xserver.frontend.util.Config

case class User(id: Int, name: String, token: String)

object UserManager {
  val current = new UserManager {
    implicit val app = Play.current

    val hasher = Play.mode match {
      case Mode.Prod => Sha512Hasher
      case _ => NoOpHasher
    }

    val tokenGenerator = Play.mode match {
      case Mode.Prod => RandomTokenGenerator
      case _ => SimpleTokenGenerator
    }
  }
}

trait UserManager {
  protected val hasher: Hasher
  protected val tokenGenerator: TokenGenerator

  private val maxUsers = Config.getInt("application.max_connections")
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

object SimpleTokenGenerator extends TokenGenerator {
  override def generateToken(username: String, password: String): String = {
    username + password
  }
}

object RandomTokenGenerator extends TokenGenerator {
  override def generateToken(username: String, password: String): String = {
    val randomString = Random.nextString(Config.getInt("auth.random-string-length"))
    val timeStamp = (System.currentTimeMillis.toString)
    username + randomString + timeStamp + password
  }
}

trait Hasher {
  def hash(str: String): String
}

object NoOpHasher extends Hasher {
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

object Sha512Hasher extends DigestHasher {
  override val hashAlgorithm = "SHA-512"
}
