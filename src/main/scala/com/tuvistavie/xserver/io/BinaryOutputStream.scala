package com.tuvistavie.xserver.io

import java.io.OutputStream
import java.io.DataOutputStream

class BinaryOutputStream(val outputStream: OutputStream, val bigEndian: Boolean) extends DataOutputStream(outputStream)
