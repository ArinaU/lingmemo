package models

sealed trait Language
case object English extends Language
case object German extends Language
case object Russian extends Language

sealed trait Translation {
  def term: String
  def definitions: Seq[String]
}

case class EnglishRussianTranslation(term: String, definitions: Seq[String]) extends Translation
case class GermanRussianTranslation(term: String, definitions: Seq[String]) extends Translation

case class User(name: String, email: String, password: String)

sealed trait UserVocabulary {
  def user: User
  def term: Translation
}


case class Document(name: String, file: String)

