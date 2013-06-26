package com.tuvistavie.xserver.backend.util

import akka.util.{ ByteStringBuilder, ByteIterator }


class ExtendedByteStringBuilder(builder: ByteStringBuilder) {
  import Conversions._

  def fill(n: Int) {
    (1 to n) foreach { _ => builder.putByte(0) }
  }

  def putPadding(n: Int) {
    fill(n.padding)
  }

  def putNum(n: Int, byteNum: Int)(implicit endian: java.nio.ByteOrder) = byteNum match {
    case 1 => builder putByte n
    case 2 => builder putShort n
    case 4 => builder putInt n
  }

  def putIntList(list: List[Int], byteNum: Int)(implicit endian: java.nio.ByteOrder) {
    list foreach { putNum(_, byteNum) }
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


  def getByte(unsigned: Boolean = false) = {
    val n = iterator.getByte
    if(unsigned) n & 0xff
    else n
  }

  def getShort(unsigned: Boolean = false)(implicit endian: java.nio.ByteOrder) = {
    val n = iterator.getShort
    if(unsigned) n & 0xffff
    else n
  }

  def getInt(unsigned: Boolean = false)(implicit endian: java.nio.ByteOrder) = {
    val n = iterator.getInt
    if(unsigned) n & 0xffffffff
    else n
  }

  def getPaddedByte(unsigned: Boolean = false) = {
    val n = getByte(unsigned)
    skip(3)
    n
  }

  def getPaddedShort(unsigned: Boolean = false)(implicit endian: java.nio.ByteOrder) = {
    val n = iterator.getShort
    skip(2)
    n
  }

  def getPaddedVal(byteNum: Int, unsigned: Boolean = false)(implicit endian: java.nio.ByteOrder) = byteNum match {
    case 1 => getPaddedByte(unsigned)
    case 2 => getPaddedShort(unsigned)
    case 4 => getInt(unsigned)
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
