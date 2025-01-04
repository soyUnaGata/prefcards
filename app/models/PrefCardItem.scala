package models

import play.api.db.slick.HasDatabaseConfigProvider
import slick.lifted.ProvenShape

import scala.concurrent.Future

case class PrefCardItem(id: Long, name:String,
                     operation: String, tools: String,
                     duration: String)


trait PrefCardTable extends HasDatabaseConfigProvider[slick.jdbc.JdbcProfile] {

  import profile.api._
  val prefcardQuery: TableQuery[PrefcardsMapping] = TableQuery[PrefcardsMapping]
  private[models] class PrefcardsMapping(tag: Tag) extends Table[PrefCardItem](tag, "prefcards") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    def operation: Rep[String] = column[String]("operation")
    def tools: Rep[String] = column[String]("tools")
    def duration: Rep[String] = column[String]("duration")
    def * : ProvenShape[PrefCardItem] = (id, name, operation, tools, duration) <>(PrefCardItem.tupled, PrefCardItem.unapply)
  }

  def insertPrefCard(prefCard: PrefCardItem): Future[Int] = {
    db.run(prefcardQuery += prefCard)
  }

  def getPrefCards: Future[Seq[PrefCardItem]] = {
    db.run(prefcardQuery.result)
  }

  def getPrefCardById(id: Long): Future[Option[PrefCardItem]] = {
    db.run(prefcardQuery.filter(_.id === id).result.headOption)
  }

  def updatePrefCard(prefCard: PrefCardItem): Future[Int] = {
    db.run(prefcardQuery.filter(_.id === prefCard.id).update(prefCard))
  }

  def deletePrefCard(id: Long): Future[Int] = {
    db.run(prefcardQuery.filter(_.id === id).delete)
  }
}


