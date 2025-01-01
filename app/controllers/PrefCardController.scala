package controllers

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.PrefCardItem
import play.api.mvc.{BaseController, ControllerComponents}

import javax.inject.Inject
import scala.collection.mutable

import play.api.libs.json._


class PrefCardController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController{
  private val prefCard = new mutable.ListBuffer[PrefCardItem]()
  prefCard += PrefCardItem( 1, "Dr. Ivanov", "Orthopedics", "Scalpel, Clamp, Needle Holder", "90 minutes")
  prefCard += PrefCardItem( 2, "Dr. Smirnov", "Cardiac Surgery", "Scalpel, Suture Material, ECG Machine", "120 minutes")

  implicit val prefCardJson: OFormat[PrefCardItem] = Json.format[PrefCardItem]

}
