package com.tuvistavie.xserver.io

import akka.actor._

object Server extends Actor {
  def receive() = {
    case s => println(s)
  }

}
