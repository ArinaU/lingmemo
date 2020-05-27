package dao

import models.User
import slick.jdbc.PostgresProfile.api._
import slick.lifted

import scala.concurrent.ExecutionContext.Implicits.global


class UserDAO(tag: Tag) extends Table[User](tag, "user") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def email = column[String]("email")

  def password = column[String]("password")

  override def * = (name, email, password, id) <> (User.tupled, User.unapply)

}

object UserDAO {

  val users = lifted.TableQuery[UserDAO]
  val db = Database.forConfig("db")

  def create(user: User) = db.run {
    users += user
  }.map { _ => () } // map() here to transform into Unit

  def getWithPassword(email: String, password: String) = db.run {
    users.filter(x => {
      x.email === email && x.password === password
    }).result.headOption //head() - take only first result
  }

  def get(email: String) = db.run {
    users.filter(x => {
      x.email === email
    }).result.headOption
  }


}
