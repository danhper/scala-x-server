package com.tuvistavie.xserver.frontend.controllers

import play.api._
import play.api.mvc._

import com.tuvistavie.xserver.frontend.xbridge.BridgeManager
import com.tuvistavie.xserver.frontend.forms._
import com.tuvistavie.xserver.frontend.auth.{ UnixAuthentication, SimpleTokenAuthentication }

object Application extends Controller with UnixAuthentication with SimpleTokenAuthentication {

  val indexRoute = com.tuvistavie.xserver.frontend.controllers.routes.Application.index
  val loginRoute = com.tuvistavie.xserver.frontend.controllers.routes.Application.login

  def index = Action { implicit request =>
    session.get("auth-token") match {
      case Some(tok) => {
        authenticate(tok) match {
          case Some(user) => {
            BridgeManager.create(user)
            Ok(views.html.index("Your new application is ready."))
          }
          case None => Redirect(loginRoute)
        }
      }
      case None => Redirect(loginRoute)
    }
  }

  def login = Action {
    Ok(views.html.login())
  }

  def doLogin() = Action { implicit request =>
    loginForm.bindFromRequest.fold (
      formWithErrors => BadRequest(views.html.login()), {
        case (username, password) => authenticate(username, password) match {
          case Some(user) => Redirect(indexRoute).withSession(
              "auth-token" -> user.token
            )
          case None => BadRequest(views.html.login())
        }
      }
    )
  }
}
