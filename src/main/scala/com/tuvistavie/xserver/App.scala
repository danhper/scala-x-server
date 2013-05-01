package com.tuvistavie.xserver

import net.liftweb.json.DefaultFormats
import net.liftweb.json.CustomSerializer
import net.liftweb.json.JInt
import net.liftweb.json.Serialization
import net.liftweb.json.NoTypeHints
import net.liftweb.json.Serialization.write

import com.tuvistavie.xserver.protocol._

case class Person(name: String, age: Int8, foo: Int16, bar: Int32)

class IntSerializer extends CustomSerializer[IntValue](format => (
  {
    case JInt(s) => Int8(s.toInt)
  },
  {
    case i: IntValue => JInt(i.value)
  }
))

object App {
  def main(args: Array[String]) = {
    implicit val formats = Serialization.formats(NoTypeHints) + new IntSerializer

    val foo = Person("foo", 8, 7, 4)
    println(write(foo))
  }
}
