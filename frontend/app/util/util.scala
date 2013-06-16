package com.tuvistavie.xserver.frontend.util

import play.api.Play

object Config {
  private val conf = Play.current.configuration

  def getString(s: String) = conf.getString(s).get
  def getInt(s: String) = conf.getInt(s).get
}
