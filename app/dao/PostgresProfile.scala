package dao

import com.github.tminglei.slickpg._

trait PostgresProfile extends ExPostgresProfile with PgArraySupport {

  override val api = appAPI

  object appAPI extends API with ArrayImplicits
}

object PostgresProfile extends PostgresProfile
