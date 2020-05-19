package models

import slick.jdbc.PostgresProfile.api._
import slick.lifted
import scala.concurrent.ExecutionContext.Implicits.global


class UserRepository(tag: Tag) extends Table[User](tag, "user") {

      def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
      def name = column[String]("name")
      def email = column[String]("email")
      def password = column[String]("password")

      override def * = (name, email, password, id) <> (User.tupled, User.unapply)

}

object UserRepositoryHelper {

  val users = lifted.TableQuery[UserRepository]
  val db = Database.forConfig("db")

  def insert(user: User) = db.run { users += user }.map { _ => () } // transform into Unit

  def find(email: String, password: String) = db.run {
    users.filter( x => { x.email === email && x.password === password }).result.headOption //head() - take only first result
  }

}
