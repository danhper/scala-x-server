package com.tuvistavie.xserver.backend.util

case class InitSettings (
  displayNumber: Int = 1,
  standAlone: Boolean = false,
  rootHeight: Int = 800,
  rootWidth: Int = 600
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

    opt[Int]('w', "root-width") valueName("<root-width>") action { (x, c) =>
      c.copy(rootWidth = x)
    } validate { x =>
      if(x >= Config.getInt("server.screen.min-width")) success
      else failure("<root-width> must >=640")
    } text("set the width for the root window")


    opt[Int]('h', "root-height") valueName("<root-height>") action { (x, c) =>
      c.copy(rootWidth = x)
    } validate { x =>
      if(x >= Config.getInt("server.screen.min-height")) success
      else failure("<root-height> must >=480")
    } text("set the height for the root window")

    opt[Unit]('s', "stand-alone") action { (_, c) =>
      c.copy(standAlone = true)
    } text("disable remote actors")

    help("help") text("prints this usage text")
  }
  def parse(args: Array[String]) = parser.parse(args, InitSettings())
}


