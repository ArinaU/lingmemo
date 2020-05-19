package controllers

import java.io.File
import java.nio.file.Paths

import play.api.mvc._
import models._
import models.UserRepositoryHelper
import com.google.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

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
        val query = UserRepositoryHelper.find(data.email, data.password)
        query.map { q => q.map { _ => Redirect(routes.HomeController.index()) }
          .getOrElse(NotFound("Oh no!")) }
      }
    )
  }


  def addUser= Action.async { implicit request: Request[AnyContent] =>
    UserRegisterForm.registerForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Redirect(routes.HomeController.index()))
      },
      data => {
        val newUser = User(data.name, data.email, data.password)
        UserRepositoryHelper.insert(newUser).map( _ => Redirect(routes.HomeController.index()))
      }
    )
  }


  def index = Action {
    Ok(views.html.index())
  }

  def uploadFileForm = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.fileForm(FileUploadForm.form))
  }

  def parseFile(file: String) = Action {
    val source = scala.io.Source.fromFile(file)
    val text = try source.mkString finally source.close()
    Ok(views.html.parsedPage(text))
  }

  def addToDictionary = Action { request =>
    val words = request.body match {
      case AnyContentAsJson(json) => json("data")
    }
    Ok("ok")
  }

  def submitFileForm = Action(parse.multipartFormData) { request =>
    request.body.file("name").map { file =>
        val filepath = s"/Users/arina/Pictures/${Paths.get(file.filename).getFileName}"
        file.ref.moveFileTo(new File(filepath))
        Ok("File uploaded")
        Redirect(routes.HomeController.parseFile(filepath))
      }
      .getOrElse {
        Redirect(routes.HomeController.index).flashing("error" -> "Missing file")
      }
  }



}



