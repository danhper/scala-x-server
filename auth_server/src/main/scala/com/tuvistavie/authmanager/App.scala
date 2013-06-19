package com.tuvistavie.authmanager

import com.typesafe.scalalogging.slf4j.Logging

object App extends Logging {
  def main(args: Array[String]) {
    val server = AuthManager.ref // explicit call to avoid lazy evaluation
    logger.info(s"server starting with path ${AuthManager.ref.path}")
  }
}
