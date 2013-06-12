package com.tuvistavie.xserver.frontend.xbridge

import scala.sys.process.Process
import play.api.Play

class Bridge (
) {
  private var initialized = false

  def launch(id: Int, username: String): Unit = {
    val backendBinaryPath = Play.current.configuration.getString("paths.backend").get
    val process = Process(Seq(backendBinaryPath, "start", "--", "-n", id.toString), None, "RUN_USER" -> username)
    process.run()
  }
}

