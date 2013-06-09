package com.tuvistavie.xserver.backend.model

class Keyboard private (val keymap: Keymap) {

}

object Keyboard {
  var current: Keyboard = makeDefaultKeyboard
  def minCode = current.keymap.minCode
  def maxCode = current.keymap.maxCode

  def makeDefaultKeyboard: Keyboard = {
    new Keyboard(new keymap.Us())
  }
}
