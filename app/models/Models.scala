package models

sealed trait Language

case object English extends Language

case object German extends Language

case object Russian extends Language

sealed trait Translation {
  def term: String

  def definitions: Seq[String]
}

case class EngRusTranslation(term: String, definitions: Seq[String]) extends Translation

case class DeRusTranslation(term: String, definitions: Seq[String]) extends Translation

case class User(name: String, email: String, password: String, id: Long = 0L)

case class Document(name: String, file: String)

sealed trait WordList {
  def userId: Long

  def unknownWords: List[String]

  def id: Long
}

case class DeRusWordList(userId: Long, unknownWords: List[String] = List(), id: Long = 0L) extends WordList


case class EngRusWordList(userId: Long, unknownWords: List[String], id: Long = 0L) extends WordList {
}

object EngRusWordList {

  def apply(userId: Long): EngRusWordList = {
    this (userId, List())
  }

  def mapperTo(userId: Long, unknownWords: List[String], id: Long) = apply(userId, unknownWords, id)

}





