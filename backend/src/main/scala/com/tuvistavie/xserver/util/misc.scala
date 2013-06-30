package com.tuvistavie.xserver.backend

import com.typesafe.config.ConfigFactory

package object util {
  val Config = ConfigFactory.load()
  lazy val RuntimeConfig = getRuntimeConfig
  private var runtimeConfig: Option[InitSettings] = None
  private def getRuntimeConfig = runtimeConfig.get
  def loadRuntimeConfig(conf: InitSettings) = runtimeConfig = Some(conf)
}

package util {
  object Path {
    val sep = java.io.File.separatorChar

    def removeTrailingSep(path: String) = path.last match {
      case `sep` => path dropRight 1
      case _ => path
    }

    def join(paths: String*) = {
      paths map removeTrailingSep mkString sep.toString
    }
  }
}
