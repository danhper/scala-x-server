package com.tuvistavie.xserver.protocol

import akka.util.ByteString

import com.tuvistavie.xserver.io.ByteOrder

trait ByteSerializable {
  implicit val endian: java.nio.ByteOrder
  def toBytes: ByteString
}

trait ByteDeserializable[T] {
  def fromBytes(b: ByteString): T
}
