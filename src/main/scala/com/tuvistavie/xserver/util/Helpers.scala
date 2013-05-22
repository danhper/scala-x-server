package com.tuvistavie.xserver.util

import akka.util.ByteStringBuilder

class IntWithPad(val i: Int) {
  def padding = (4 - (i % 4)) % 4
  def withPadding = i + padding
}

object IntWithPad {
  implicit def intToIntWithPad(i: Int) = new IntWithPad(i)
}

class ExtendedByteStringBuilder(builder: ByteStringBuilder) {
  import IntWithPad._

  def fill(n: Int) {
    (1 to n) foreach { _ => builder.putByte(0) }
  }

  def writePadding(n: Int) {
    fill(n.padding)
  }
}

object ExtendedByteStringBuilder {
  implicit def byteStringBuilderToExtendedByteStringBuilder(builder: ByteStringBuilder) = new ExtendedByteStringBuilder(builder)
}
