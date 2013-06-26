package com.tuvistavie.xserver.protocol.request

import akka.util.ByteIterator
import com.tuvistavie.xserver.backend.util.ExtendedByteIterator
import scala.collection.mutable.MapBuilder

trait ValueGenerator {
  import ExtendedByteIterator._

  case class ValueInfo (
    mask: Int,
    name: String,
    byteNum: Int,
    unsigned: Boolean = false
  )

  def values: List[ValueInfo]

  def getValues(mask: Int, iterator: ByteIterator)(implicit endian: java.nio.ByteOrder) = {
    val mapBuilder = new MapBuilder[String, Int, Map[String, Int]](Map.empty)

    values foreach { v =>
      if((v.mask & mask) != 0)
        mapBuilder += (v.name -> iterator.getPaddedVal(v.byteNum, v.unsigned))
    }

    mapBuilder result
  }
}
