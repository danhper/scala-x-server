package com.tuvistavie.xserver.backend.model

class Extension (
  val majorOpcode: Int,
  val firstEvent: Int,
  val firstError: Int
)

object Extension {
  private var availableExtensions: Map[String, Extension] = Map()

  def register(name: String, extension: Extension) = availableExtensions += (name -> extension)

  def getExtension(name: String) = availableExtensions(name)

  def isAvailable(name: String) = availableExtensions.contains(name)
}
