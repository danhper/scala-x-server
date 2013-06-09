package com.tuvistavie.xserver.backend.io

abstract sealed trait ByteOrder {
  implicit val endian: java.nio.ByteOrder
}

trait BigEndian extends ByteOrder {
  implicit val endian: java.nio.ByteOrder = java.nio.ByteOrder.BIG_ENDIAN
}

trait LittleEndian extends ByteOrder {
  implicit val endian: java.nio.ByteOrder = java.nio.ByteOrder.LITTLE_ENDIAN
}
