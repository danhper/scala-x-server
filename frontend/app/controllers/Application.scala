package com.tuvistavie.xserver.frontend.controllers

import play.api.{ Play, Mode }
import play.api.mvc.{ Controller, Action, WebSocket }
import play.api.libs.json.{ JsValue, JsObject, JsString }
import play.api.libs.iteratee.{ Done, Input, Enumerator }
import play.api.Logger

import com.tuvistavie.xserver.bridge.BridgeManager
import com.tuvistavie.xserver.frontend.forms.loginForm
import com.tuvistavie.xserver.frontend.auth.{ NixLoginManager, DummyLoginManager, UserManager }
import com.tuvistavie.xserver.frontend.util.Config

object Application extends Controller {
  implicit val app = Play.current

  val loginManager = Play.mode match {
    case Mode.Prod => NixLoginManager
    case _ => DummyLoginManager
  }

  val indexRoute = com.tuvistavie.xserver.frontend.controllers.routes.Application.index
  val loginRoute = com.tuvistavie.xserver.frontend.controllers.routes.Application.login

  def index = Action { implicit request =>
    loginManager.login match {
      case Some(user) => {
        BridgeManager.create(user)
        Ok(views.html.index())
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
        case (username, password, properties) => loginManager.authenticate(username, password) match {
          case Some(user) => {
            Logger.debug("registering user properties" + properties)
            val registeredUser = UserManager.current registerUser(username, password, properties)
            Redirect(indexRoute).withSession (
              Config.getString("auth.token-name") -> registeredUser.token
            )
          }
          case None => BadRequest(views.html.login())
        }
      }
    )
  }

  def connect = WebSocket.async[JsValue] { implicit request =>
    BridgeManager.connect(loginManager.login)
  }
}
