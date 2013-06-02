package com.tuvistavie.xserver.model

import com.tuvistavie.xserver.util.Properties.{ settings => Config }

object ServerInfo {

  val bitsForResources = 22
  val bitsForClients = 7

  val maxClients = 1 << bitsForClients

  val clientOffset = bitsForResources
  val clientMask = ((1 << bitsForClients) - 1) << bitsForResources
  val idMask = (1 << bitsForResources) - 1
}
