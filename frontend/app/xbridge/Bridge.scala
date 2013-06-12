package com.tuvistavie.xserver.frontend.xbridge

import scala.sys.addShutdownHook
import scala.sys.process.Process
import play.api.Play

class Bridge (
  username: String
) {
  private var initialized = false
  private val shutdownHook = addShutdownHook(stopServer)

  private val binPath = Play.current.configuration.getString("paths.backend").get

  def launch(id: Int) {
    val args = List("start", "--", "-n", id.toString)
    val process = Process(binPath :: args, None, "RUN_USER" -> username)
    process.run()
  }

  def stop() {
    stopServer()
    shutdownHook.remove()
  }

  private def stopServer() {
    Process(Seq(binPath, "stop"), None, "RUN_USER" -> username).!
  }
}

