package com.tuvistavie.xserver.backend.util

import akka.util.{ ByteStringBuilder, ByteIterator }


class ExtendedByteStringBuilder(builder: ByteStringBuilder) {
  import Conversions._

  def fill(n: Int) {
    (1 to n) foreach { _ => builder.putByte(0) }
  }

  def writePadding(n: Int) {
    fill(n.padding)
  }

  def putBoolean(b: Boolean) {
    if(b) builder.putByte(1)
    else builder.putByte(0)
  }
}

object ExtendedByteStringBuilder {
  implicit def byteStringBuilderToExtendedByteStringBuilder(builder: ByteStringBuilder) = new ExtendedByteStringBuilder(builder)
}

class ExtendedByteIterator(iterator: ByteIterator) {
  import Conversions._

  def skip(n: Int) {
    (1 to n) foreach { _ => iterator getByte }
  }

  def skipPadding(n: Int) {
    skip(n padding)
  }

  def getPaddedByte = {
    val n = iterator.getByte
    skip(3)
    n
  }

  def getPaddedShort(implicit endian: java.nio.ByteOrder) = {
    val n = iterator.getShort
    skip(2)
    n
  }

  def getString(n: Int): String = {
    val byteArray: Array[Byte] = new Array[Byte](n)
    iterator.getBytes(byteArray)
    new String(byteArray)
  }
}

object ExtendedByteIterator {
  implicit def byteIteratorToExtendedByteIterator(iterator: ByteIterator) = new ExtendedByteIterator(iterator)
}
