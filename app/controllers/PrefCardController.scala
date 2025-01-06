package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import models.{PrefCardItem, PrefCardTable}
import play.api.db.slick.DatabaseConfigProvider

@Singleton
class PrefCardController @Inject()(cc: ControllerComponents, dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  val prefCardTable = new PrefCardTable(dbConfigProvider)

  def addCard() = Action.async(parse.json) { request =>
    request.body.validate[PrefCardItem].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> "Invalid data", "errors" -> JsError.toJson(errors)))),
      prefCard => {
        prefCardTable.insertPrefCard(prefCard).map { _ =>
          Ok(Json.obj("message" -> "PrefCard added"))
        }
      }
    )
  }

  def getAll = Action.async {
    prefCardTable.getPrefCards.map { prefCards =>
      Ok(Json.toJson(prefCards))
    }
  }

  def getById(id: Long) = Action.async {
    prefCardTable.getPrefCardById(id).map {
      case Some(prefCard) => Ok(Json.toJson(prefCard))
      case None => NotFound(Json.obj("message" -> "PrefCard not found"))
    }
  }

  def updateCard(id: Long) = Action.async(parse.json) { request =>
    request.body.validate[PrefCardItem].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> "Invalid data", "errors" -> JsError.toJson(errors)))),
      prefCard => {
        prefCardTable.updatePrefCard(prefCard).map { _ =>
          Ok(Json.obj("message" -> "PrefCard updated"))
        }
      }
    )
  }

  def deleteCard(id: Long) = Action.async {
    prefCardTable.deletePrefCard(id).map {
      case 0 => NotFound(Json.obj("message" -> "PrefCard not found"))
      case _ => NoContent
    }
  }
}
