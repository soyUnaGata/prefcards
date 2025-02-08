package models

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.lifted.ProvenShape
import play.api.libs.json._
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import io.swagger.annotations._

@ApiModel(description = "Details about a PrefCard item")
case class PrefCardItem(
                         @ApiModelProperty(value = "Unique ID of the PrefCard", required = true, example = "1")
                         id: Option[Long],

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

@Api(value = "PrefCard Management", description = "Operations related to PrefCards")
class PrefCardTable @Inject()(
                               protected val dbConfigProvider: DatabaseConfigProvider
                             )(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val prefcardQuery: TableQuery[PrefcardsMapping] = TableQuery[PrefcardsMapping]

  @ApiModel(description = "PrefCard table mapping")
  private[models] class PrefcardsMapping(tag: Tag) extends Table[PrefCardItem](tag, "prefcards") {
    @ApiModelProperty(value = "Primary key of the PrefCard", required = true)
    def id: Rep[Option[Long]] = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

    @ApiModelProperty(value = "Name of the PrefCard", required = true)
    def name: Rep[String] = column[String]("name")

    @ApiModelProperty(value = "Operation associated with the PrefCard", required = true)
    def operation: Rep[String] = column[String]("operation")

    @ApiModelProperty(value = "Tools required for the operation", required = true)
    def tools: Rep[String] = column[String]("tools")

    @ApiModelProperty(value = "Duration of the operation", required = true)
    def duration: Rep[String] = column[String]("duration")

    def * : ProvenShape[PrefCardItem] =
      (id, name, operation, tools, duration) <> ((PrefCardItem.apply _).tupled, PrefCardItem.unapply)
  }

  @ApiOperation(value = "Insert a new PrefCard", notes = "Adds a new PrefCard to the database")
  def insertPrefCard(
                      @ApiParam(value = "PrefCard details to insert", required = true) prefCard: PrefCardItem
                    ): Future[Option[Long]] = {
    val newPrefCard = prefCard.copy(id = None)
    db.run(prefcardQuery returning prefcardQuery.map(_.id) += newPrefCard)
  }


  @ApiOperation(value = "Get all PrefCards", notes = "Retrieves a list of all PrefCards")
  def getPrefCards: Future[Seq[PrefCardItem]] = {
    db.run(prefcardQuery.result)
  }

  @ApiOperation(
    value = "Get a PrefCard by ID",
    notes = "Retrieves details of a PrefCard by its unique ID"
  )
  def getPrefCardById(
                       @ApiParam(value = "Unique ID of the PrefCard", required = true) id: Long
                     ): Future[Option[PrefCardItem]] = {
    db.run(prefcardQuery.filter(_.id === id).result.headOption)
  }

  @ApiOperation(value = "Update a PrefCard", notes = "Updates details of an existing PrefCard")
  def updatePrefCard(
                      @ApiParam(value = "Updated PrefCard details", required = true) prefCard: PrefCardItem
                    ): Future[Int] = {
    prefCard.id match {
      case Some(cardId) =>
        db.run(prefcardQuery.filter(_.id === cardId).update(prefCard))
      case None =>
        Future.failed(new IllegalArgumentException("Cannot update PrefCard without an ID"))
    }
  }

  @ApiOperation(value = "Delete a PrefCard", notes = "Deletes a PrefCard by its unique ID")
  def deletePrefCard(
                      @ApiParam(value = "Unique ID of the PrefCard to delete", required = true) id: Long
                    ): Future[Int] = {
    db.run(prefcardQuery.filter(_.id === id).delete)
  }
}



