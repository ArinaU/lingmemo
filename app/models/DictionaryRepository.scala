package models

import slick.jdbc.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration._


trait Profile {
  val profile: JdbcProfile
}

trait PostgresModule { self: Profile =>
  import slick.jdbc.PostgresProfile.api._

//
//  class UserVocabularyTable(tag: Tag) extends Table[UserVocabulary](tag, "UserVocabulary") {
//    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//    def word = column[String]("word")
//    def meaning = column[String]("meaning")
//    def * = (word, meaning, id).mapTo[UserVocabulary] }

}

object DictionaryRepository extends App {


// }
}