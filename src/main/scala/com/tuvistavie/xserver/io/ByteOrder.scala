package com.tuvistavie.xserver.io

abstract sealed trait ByteOrder {
  implicit val endian: java.nio.ByteOrder
}

trait BigEndian extends ByteOrder {
  implicit val endian: java.nio.ByteOrder = java.nio.ByteOrder.BIG_ENDIAN
}

object BigEndian extends BigEndian

trait LittleEndian extends ByteOrder {
  implicit val endian: java.nio.ByteOrder = java.nio.ByteOrder.LITTLE_ENDIAN
}

object LittleEndian extends LittleEndian
