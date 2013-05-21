package com.tuvistavie.xserver.io

import akka.actor._

class Server extends Actor {
  def receive() = {
    case s => println(s)
  }

}
