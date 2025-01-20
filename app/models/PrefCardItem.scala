package models

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.lifted.ProvenShape
import play.api.libs.json._
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import io.swagger.annotations.{ApiModel, ApiModelProperty}

@ApiModel(description = "Details about a PrefCard item")
case class PrefCardItem(
                         @ApiModelProperty(value = "Unique ID of the PrefCard", required = true, example = "1")
                         id: Long,

                         @ApiModelProperty(value = "Name of the PrefCard", required = true, example = "Surgical Tools")
                         name: String,

                         @ApiModelProperty(value = "Operation associated with the PrefCard", required = true, example = "Appendectomy")
                         operation: String,

                         @ApiModelProperty(value = "Tools required for the operation", required = true, example = "Scalpel, Forceps")
                         tools: String,

                         @ApiModelProperty(value = "Duration of the operation", required = true, example = "2 hours")
                         duration: String
                       )


object PrefCardItem {
  implicit val prefCardItemReads: Reads[PrefCardItem] = Json.reads[PrefCardItem]
  implicit val prefCardItemWrites: Writes[PrefCardItem] = Json.writes[PrefCardItem]
}

class PrefCardTable @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  val prefcardQuery: TableQuery[PrefcardsMapping] = TableQuery[PrefcardsMapping]
  private[models] class PrefcardsMapping(tag: Tag) extends Table[PrefCardItem](tag, "prefcards") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    def operation: Rep[String] = column[String]("operation")
    def tools: Rep[String] = column[String]("tools")
    def duration: Rep[String] = column[String]("duration")
    def * : ProvenShape[PrefCardItem] = (id, name, operation, tools, duration) <>((PrefCardItem.apply _).tupled, PrefCardItem.unapply)
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


