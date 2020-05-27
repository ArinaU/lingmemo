package models

import play.api.data.Form
import play.api.data.Forms._


case class FileUploadForm(name: String)

object FileUploadForm {
  val form: Form[FileUploadForm] = Form(
    mapping(
      "name" -> text
    )(FileUploadForm.apply)(FileUploadForm.unapply)
  )
}

case class UserRegisterForm(name: String, email: String, password: String)

object UserRegisterForm {
  val registerForm: Form[UserRegisterForm] = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserRegisterForm.apply)(UserRegisterForm.unapply)
  )
}

case class UserLoginForm(email: String, password: String)

object UserLoginForm {
  val loginForm: Form[UserLoginForm] = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserLoginForm.apply)(UserLoginForm.unapply)
  )
}




