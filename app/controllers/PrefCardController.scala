package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import models.{PrefCardTable, PrefCardItem}

@Singleton
class PrefCardController @Inject()(cc: ControllerComponents, prefCardTable: PrefCardTable)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def addCard = Action.async(parse.json) { request =>
    request.body.validate[PrefCardItem].fold(
      errors => Future.successful(BadRequest("Invalid data")),
      prefCard => {
        prefCardTable.insertPrefCard(prefCard).map { _ =>
          Ok("PrefCard added")
        }
      }
    )
  }

  def getAll = Action.async {
    prefCardTable.getPrefCards.map { prefCards =>
      Ok(views.html.prefcards(prefCards))
    }
  }

  def getById(id: Long) = Action.async {
    prefCardTable.getPrefCardById(id).map {
      case Some(prefCard) => Ok(views.html.prefcard(prefCard))
      case None => NotFound("PrefCard not found")
    }
  }

  def updateCard(id: Long) = Action.async(parse.json) { request =>
    request.body.validate[PrefCardItem].fold(
      errors => Future.successful(BadRequest("Invalid data")),
      prefCard => {
        prefCardTable.updatePrefCard(prefCard).map { _ =>
          Ok("PrefCard updated")
        }
      }
    )
  }

  def deleteCard(id: Long) = Action.async {
    prefCardTable.deletePrefCard(id).map { _ =>
      Ok("PrefCard deleted")
    }
  }
}
