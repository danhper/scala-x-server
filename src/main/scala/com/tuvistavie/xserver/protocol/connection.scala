package com.tuvistavie.xserver.protocol

import java.io.DataInputStream
import com.tuvistavie.xserver.io._


object Connection {
  def initialize(stream: DataInputStream) {
    val endian = stream.readChar()
    stream.readByte() // skip byte
    val byteOrderSafeStream = BinaryInputStream(stream, endian == 'B')
    readConnectionData(byteOrderSafeStream)
  }

  private def readConnectionData(stream: BinaryInputStream) = {
    val protocolMajorVersion = stream.readCard16()
    val protocolMinorVersion = stream.readInt16()
    val n = stream.readCard16()
    val d = stream.readCard16()
    stream.skip(2)
    // check for protocol version and no authentication
    protocolMajorVersion.value == 11 && protocolMinorVersion.value == 0 && n.value == 0 && d.value == 0
  }
}

