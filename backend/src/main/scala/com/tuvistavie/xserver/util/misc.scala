package com.tuvistavie.xserver.backend

import com.typesafe.config.ConfigFactory

package object util {
  val Config = ConfigFactory.load()
  lazy val RuntimeConfig = getRuntimeConfig
  private var runtimeConfig: Option[InitSettings] = None
  private def getRuntimeConfig = runtimeConfig.get
  def loadRuntimeConfig(conf: InitSettings) = runtimeConfig = Some(conf)
}
