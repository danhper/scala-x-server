package com.tuvistavie.xserver.frontend.controllers

import play.api._
import play.api.mvc._

import com.tuvistavie.xserver.frontend.forms._
import com.tuvistavie.xserver.frontend.auth.UnixAuthentication

object Application extends Controller with UnixAuthentication {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def login = Action {
    Ok(views.html.login())
  }

  def doLogin() = Action { implicit request =>
    loginForm.bindFromRequest.fold (
      formWithErrors => BadRequest(views.html.login()),
      b => Redirect(com.tuvistavie.xserver.frontend.controllers.routes.Application.index)
    )
  }
}
