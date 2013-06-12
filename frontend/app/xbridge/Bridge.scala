package com.tuvistavie.xserver.frontend.xbridge

import scala.sys.process.Process
import play.api.Play

class Bridge (
) {
  private var initialized = false

  def launch(id: Int, username: String): Unit = {
    val binPath = Play.current.configuration.getString("paths.backend").get
    val args = List("start", "--", "-n", id.toString)
    val process = Process(binPath :: args, None, "RUN_USER" -> username)
    process.run()
  }
}

