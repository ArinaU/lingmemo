package dao

import models._
import PostgresProfile.api._
import slick.lifted

import scala.concurrent.ExecutionContext.Implicits.global
import UserDAO._
import slick.dbio.DBIOAction

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class EngRusLearningWordListDAO(tag: Tag) extends Table[EngRusLearningWordList](tag, "eng_rus_learning_word_list") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("user_id")
  def unknownWords = column[List[String]]("unknown_words")
  def user = foreignKey("user_FK", userId, users)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)


  override def * = (userId, unknownWords, id) <> ((EngRusLearningWordList.mapperTo _).tupled, EngRusLearningWordList.unapply)
}


object EngRusLearningWordListDAO {
  val lists = lifted.TableQuery[EngRusLearningWordListDAO]
  val db = Database.forConfig("db")

  def create(list: EngRusLearningWordList) = db.run { lists += list }.map { _ => () }

  def get(userId: Long) = db.run {
    lists.filter( x => { x.userId === userId }).result.headOption
  }

  def update(userId: Long, unknownWords: List[String]) = db.run {
    for {
      y <- lists.filter(_.userId === userId).result
      words = y.map(_.unknownWords).flatMap(_.union(unknownWords)).distinct.toList
      x <- lists.filter(_.userId === userId).map(_.unknownWords).update(words)
    } yield x
  }

}