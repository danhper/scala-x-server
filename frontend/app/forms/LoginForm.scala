package com.tuvistavie.xserver.frontend

import play.api.data._
import play.api.data.Forms._

import com.tuvistavie.xserver.frontend.auth.UserProperties


package object forms {
  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text,
      "userProperties" -> mapping(
        "windowWidth" -> number,
        "windowHeight" -> number
      )(UserProperties.apply)(UserProperties.unapply)
    )
  )
}
