package com.tuvistavie.xserver.backend.util

case class InitSettings (
  displayNumber: Int = 1
)

object InitSettings  {
  private var parser = new scopt.OptionParser[InitSettings]("scalaxs") {
    head(Config.getString("server.info.vendor"),
      Config.getString("server.info.release-version"))

    opt[Int]('n', "display-number") required() valueName("<display-number>") action { (x, c) =>
      c.copy(displayNumber = x)
    } validate { x =>
      if(x >= 0) success else failure("Value <display-number> must be >=0")
    } text("display number for the X server")

    help("help") text("prints this usage text")
  }
  def parse(args: Array[String]) = parser.parse(args, InitSettings())
}


