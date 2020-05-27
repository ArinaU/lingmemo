package controllers

import com.google.inject.{Inject, Singleton}
import dao._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index = Action {
    Ok(views.html.index())
  }

  def addToDictionary = Action.async { request =>
    val userCookie = request.cookies.get("User") match {
      case Some(cookie) => cookie.value
      case None => ""
    }
    val words = request.body match {
      case AnyContentAsJson(json) => Json.parse(json("data").toString()).as[List[String]]
      case _ => List() // error processing here
    }
    UserDAO.get(userCookie).map {
      case Some(user) => {
        println(words)
        println(EngRusWordListDAO.update(user.id, words))
        Redirect(routes.HomeController.getUserDictionary())
      }
      case None => Redirect(routes.HomeController.index()) // add functionality here
    }
  }


  def getUserDictionary = Action.async { request =>
    val userCookie = request.cookies.get("User") match {
      case Some(cookie) => cookie.value
      case None => ""
    }
    UserDAO.get(userCookie) flatMap {
      case Some(user) => {
        EngRusWordListDAO.get(user.id) flatMap {
          case Some(l) => {
            val res = l.unknownWords.toString()
            Future.successful(Ok(views.html.userDictionary(res)))
          }
          case None => Future.successful(NotFound("No result"))
        }
      }
      case None => Future.successful(NotFound("No user"))
    }
  }


}



