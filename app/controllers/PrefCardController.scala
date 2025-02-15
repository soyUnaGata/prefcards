package controllers

import io.swagger.annotations._

import models.{PrefCardItem, PrefCardTable}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json._
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
@Api(value = "/prefcards", tags = Array("PrefCard"), produces = "application/json")
class PrefCardController @Inject()(cc: ControllerComponents, dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  val prefCardTable = new PrefCardTable(dbConfigProvider)

  @ApiOperation(
    value = "Add a new PrefCard",
    notes = "Creates a new PrefCard entry in the database",
    response = classOf[PrefCardItem],
    httpMethod = "POST",
    consumes = "application/json"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "PrefCard data",
      required = true,
      dataTypeClass = classOf[PrefCardItem],
      paramType = "body"
    )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "PrefCard added successfully"),
    new ApiResponse(code = 400, message = "Invalid input data")
  ))
  def addCard(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[PrefCardItem].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> "Invalid data", "errors" -> JsError.toJson(errors)))),
      prefCard => {
        prefCardTable.insertPrefCard(prefCard).map { _ =>
          Ok(Json.obj("message" -> "PrefCard added"))
        }
      }
    )
  }

  @ApiOperation(
    value = "Get all PrefCards",
    notes = "Retrieves a list of all PrefCards",
    response = classOf[PrefCardItem],
    responseContainer = "List"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "List of PrefCards"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getAll: Action[AnyContent] = Action.async {
    prefCardTable.getPrefCards.map { prefCards =>
      Ok(Json.toJson(prefCards))
    }
  }

  @ApiOperation(
    value = "Get a PrefCard by ID",
    notes = "Retrieves a PrefCard by its unique ID",
    response = classOf[PrefCardItem]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "PrefCard retrieved"),
    new ApiResponse(code = 404, message = "PrefCard not found")
  ))
  def getById(
               @ApiParam(value = "ID of the PrefCard to retrieve", required = true) id: Long
             ): Action[AnyContent] = Action.async {
    prefCardTable.getPrefCardById(id).map {
      case Some(prefCard) => Ok(Json.toJson(prefCard))
      case None => NotFound(Json.obj("message" -> "PrefCard not found"))
    }
  }

  @ApiOperation(
    value = "Update a PrefCard",
    notes = "Updates an existing PrefCard identified by its ID",
    response = classOf[PrefCardItem]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "PrefCard updated successfully"),
    new ApiResponse(code = 400, message = "Invalid input data"),
    new ApiResponse(code = 404, message = "PrefCard not found"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "Updated PrefCard data",
      required = true,
      dataTypeClass = classOf[PrefCardItem],
      paramType = "body"
    )
  ))
  def updateCard(
                  @ApiParam(value = "ID of the PrefCard to update", required = true) id: Long
                ): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[PrefCardItem].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> "Invalid data", "errors" -> JsError.toJson(errors)))),
      prefCard => {
        prefCardTable.updatePrefCard(prefCard).map { _ =>
          Ok(Json.obj("message" -> "PrefCard updated", "data" -> Json.toJson(prefCard)))
        }
      }
    )
  }

  @ApiOperation(
    value = "Delete a PrefCard",
    notes = "Deletes a PrefCard identified by its ID",
    response = classOf[PrefCardItem]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 204, message = "PrefCard deleted successfully"),
    new ApiResponse(code = 404, message = "PrefCard not found")
  ))
  def deleteCard(
                  @ApiParam(value = "ID of the PrefCard to delete", required = true) id: Long
                ): Action[AnyContent] = Action.async {
    prefCardTable.deletePrefCard(id).map {
      case 0 => NotFound(Json.obj("message" -> "PrefCard not found"))
      case _ => NoContent
    }
  }

  @ApiOperation(
    value = "Redirect to Swagger UI",
    notes = "Redirects to the Swagger documentation page"
  )
  def redirectDocs: Action[AnyContent] = Action {
    Redirect(
      url = "/assets/swagger-ui/index.html",
      queryStringParams = Map("url" -> Seq("/assets/swagger-ui/swagger.json"))
    )
  }
}
