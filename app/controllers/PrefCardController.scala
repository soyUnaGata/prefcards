package controllers

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.PrefCardItem
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject
import scala.collection.mutable
import play.api.libs.json._


class PrefCardController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController{
  def getAll(): Action[AnyContent] = Action {
    if (prefCard.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(prefCard))
    }
  }

  private val prefCard = new mutable.ListBuffer[PrefCardItem]()
  prefCard += PrefCardItem( 1, "Dr. Ivanov", "Orthopedics", "Scalpel, Clamp, Needle Holder", "90 minutes")
  prefCard += PrefCardItem( 2, "Dr. Smirnov", "Cardiac Surgery", "Scalpel, Suture Material, ECG Machine", "120 minutes")

  implicit val prefCardJson: OFormat[PrefCardItem] = Json.format[PrefCardItem]

}
