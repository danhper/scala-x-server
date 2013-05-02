package com.tuvistavie.util

class IntTimes(n: Int) {
  def times(f: => Unit) = 1 to n foreach { _ => f }
}
object IntTimes {
  implicit def intToIntTimes(n: Int) = new IntTimes(n)
}
