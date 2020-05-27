package controllers

import com.google.inject.{Inject, Singleton}
import dao.UserDAO
import models.{User, UserLoginForm, UserRegisterForm}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def register = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.register(UserRegisterForm.registerForm))
  }

  def login = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login(UserLoginForm.loginForm))
  }

  def loginUser = Action.async { implicit request: Request[AnyContent] =>
    UserLoginForm.loginForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(NotFound("Form is incorrect"))
      },
      data => {
        val query = UserDAO.getWithPassword(data.email, data.password)
        query.map { q =>
          q.map { _ =>
            Ok(views.html.index()).withCookies(Cookie("User", data.email))
          }
            .getOrElse(NotFound("Oh no!"))
        }
      }
    )
  }


  def addUser = Action.async { implicit request: Request[AnyContent] =>
    UserRegisterForm.registerForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Redirect(routes.UserController.register()))
      },
      data => {
        val newUser = User(data.name, data.email, data.password)
        UserDAO.create(newUser).map(_ => Redirect(routes.UserController.login()))
      }
    )
  }

}
