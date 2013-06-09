package com.tuvistavie.xserver.backend.util

object Computations {
  def pixelsToMillimeters(pixels: Int): Int = math.round(pixels / 3.779528).toInt
}
