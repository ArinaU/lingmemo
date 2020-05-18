package controllers

import java.io.File
import java.nio.file.Paths

import javax.inject._
import play.api.mvc._
import models._

import scala.io.Source

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def registerUser = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.registerUser(UserRegisterForm.userForm))
  }

  def submitUser = Action(parse.form(UserRegisterForm.userForm)) { implicit request =>
    val userData = request.body
    val newUser  = models.User(userData.name, userData.email, userData.password)
    println(newUser.name)
    Redirect(routes.HomeController.index())
  }

  def index = Action {
    Ok(views.html.index())
  }

  def uploadFileForm = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.form(FileUploadForm.form))
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



