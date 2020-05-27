package controllers

import java.io.File
import java.nio.file.Paths

import com.google.inject.{Inject, Singleton}
import models.FileUploadForm
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class FileUploadController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def uploadFileForm = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.fileForm(FileUploadForm.form))
  }

  def parseFile(file: String) = Action {
    val source = scala.io.Source.fromFile(file)
    val text = try source.mkString finally source.close()
    Ok(views.html.parsedPage(text))
  }


  def submitFileForm = Action(parse.multipartFormData) { request =>
    request.body.file("name").map { file =>
      val filepath = s"/Users/arina/Pictures/${Paths.get(file.filename).getFileName}"
      file.ref.moveFileTo(new File(filepath))
      Ok("File uploaded")
      Redirect(routes.FileUploadController.parseFile(filepath))
    }
      .getOrElse {
        Redirect(routes.FileUploadController.uploadFileForm())
      }
  }


}
