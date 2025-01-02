package controllers

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.{NewPrefCardItem, PrefCardItem}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import slick.jdbc.H2Profile.api._

import javax.inject.Inject
import scala.collection.mutable
import play.api.libs.json._


class PrefCardController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController{

  private val prefCard = new mutable.ListBuffer[PrefCardItem]()
  prefCard += PrefCardItem( 1, "Dr. Ivanov", "Orthopedics", "Scalpel, Clamp, Needle Holder", "90 minutes")
  prefCard += PrefCardItem( 2, "Dr. Smirnov", "Cardiac Surgery", "Scalpel, Suture Material, ECG Machine", "120 minutes")

  implicit val prefCardJson: OFormat[PrefCardItem] = Json.format[PrefCardItem]
  implicit val newPrefCardJson: OFormat[NewPrefCardItem] = Json.format[NewPrefCardItem]


  def getAll(): Action[AnyContent] = Action {
    if (prefCard.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(prefCard))
    }
  }

  def getById(itemId: Long) = Action {
    val foundItem = prefCard.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound(Json.obj("error" -> s"Item with ID $itemId not found"))
    }
  }

  def addCard() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val prefCardItem: Option[NewPrefCardItem] =
      jsonObject.flatMap(
        Json.fromJson[NewPrefCardItem](_).asOpt
      )
    prefCardItem match {
      case Some (newItem) =>
        val nextId = prefCard.map(_.id).max + 1
        val toBeAdded = PrefCardItem(nextId, newItem.name, newItem.operation, newItem.tools, newItem.duration)
        prefCard += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None => BadRequest
    }
  }

  def editCard(itemId: Long) = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val updatedCard: Option[NewPrefCardItem] =
      jsonObject.flatMap(Json.fromJson[NewPrefCardItem](_).asOpt)

    updatedCard match {
      case Some(newItem) =>
        val index = prefCard.indexWhere(_.id == itemId)

        if (index != -1) {
          val updated = PrefCardItem(
            id = itemId,
            name = newItem.name,
            operation = newItem.operation,
            tools = newItem.tools,
            duration = newItem.duration
          )

          prefCard.update(index, updated)

          saveCards(prefCard)

          Ok(Json.toJson(updated))
        } else {
          NotFound(Json.obj("error" -> s"Item with ID $itemId not found"))
        }

      case None => BadRequest(Json.obj("error" -> "Invalid data"))
    }
  }


  def deleteCard(itemId: Long) = Action {
    val foundItem = prefCard.find(_.id == itemId)
    foundItem match {
      case Some(_) =>
        prefCard -= foundItem.get
        NoContent
      case None => NotFound(Json.obj("error" -> s"Item with ID $itemId not found"))
    }
  }

  def saveCards(card: PrefCardItem): DBIO[Int] = {
    val query = TableQuery[PrefCardItems].filter(_.id === card.id)
    query.update(card)
  }
}
