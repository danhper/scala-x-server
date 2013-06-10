package com.tuvistavie.xserver.frontend.xbridge

import scala.sys.process.Process
import play.api.Play

class Bridge (
) {
  private var initialized = false

  def launch(id: Int, username: String): Unit = {
    val backendBinaryPath = Play.current.configuration.getString("paths.backend")
    val command = "RUN_USER=%s ./%s -- -i %d".format(
      username, backendBinaryPath, id
    )
    val process = Process(command)
    process.run()
  }

}

