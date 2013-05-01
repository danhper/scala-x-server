package com.tuvistavie.xserver.protocol

import java.io.DataInputStream
import java.io.DataOutputStream

import com.tuvistavie.xserver.io._

trait ByteSerializable {
  def read(data: BinaryInputStream): Unit
  def write(data: BinaryOutputStream): Unit
}
