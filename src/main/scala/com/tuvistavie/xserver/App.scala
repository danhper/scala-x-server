package com.tuvistavie.xserver

import io.Server
import util.Properties.{ settings => Config }

import com.typesafe.scalalogging.slf4j.Logging

object App extends Logging {
  def main(args: Array[String]) = {
    logger.debug("foo")
    val displayNumber = Config.getInt("server.default-display")
    Server.startUp(displayNumber)
  }
}
