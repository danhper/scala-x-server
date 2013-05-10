package com.tuvistavie.util

trait Enumerable[T] {
  def fromValue(value: Int): T
}
