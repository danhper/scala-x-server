package com.tuvistavie.xserver.backend.util

import scala.sys.{ env, props, addShutdownHook }
import scala.sys.process.Process

object Init {

  val initFile = Config.getString("app.ini-file")

  def runInitFile() {
    val shell = env.getOrElse("SHELL", "/bin/sh")
    val home = env.getOrElse("HOME", "/home/%s".format(props("user.name")))
    val initFilePath = Path.join(home, initFile)
    val process = Process(Seq(shell, initFilePath), None, "DISPLAY" -> RuntimeConfig.display)
    val p = process.run()
    addShutdownHook { p.destroy() }
  }
}
