package controllers

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.PrefCardItem
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject
import scala.collection.mutable
import play.api.libs.json._


class PrefCardController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController{

  private val prefCard = new mutable.ListBuffer[PrefCardItem]()
  prefCard += PrefCardItem( 1, "Dr. Ivanov", "Orthopedics", "Scalpel, Clamp, Needle Holder", "90 minutes")
  prefCard += PrefCardItem( 2, "Dr. Smirnov", "Cardiac Surgery", "Scalpel, Suture Material, ECG Machine", "120 minutes")

  implicit val prefCardJson: OFormat[PrefCardItem] = Json.format[PrefCardItem]


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
      case None => NotFound
    }
  }
}
