package controllers

import javax.inject._

import scala.concurrent.ExecutionContext

import play.api.libs.json._
import play.api.libs.json.Format.GenericFormat
import play.api.mvc._
import utilities.JsonOP
import DTO.CredentialsDTO

@Singleton
class CredentialsController @Inject() (cc: ControllerComponents, credentialsService: services.CredentialsService)(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {

  def validateCredentials(): Action[AnyContent] = Action.async { implicit request =>
    credentialsService
      .validateCredentials(CredentialsDTO.decode(request.body.asJson.get.toString))
      .map { crd =>
        if (crd._1 != 0) {
          Ok("User authenticated").withCookies(new Cookie("Id", value = crd.toString))
        } else {
          NotFound(s"User not found")
        }
      }
      .recover {
        case _: NoSuchElementException =>
          NotFound(s"User not found")
        case ex: Exception =>
          InternalServerError(s"An error occurred: ${ex.getMessage}")
      }
  }

  def createCredentials(): Action[AnyContent] = Action.async { implicit request =>
    credentialsService
      .createCredentials(CredentialsDTO.decode(request.body.asJson.get.toString))
      .map { success =>
        if (success) {
          Ok(Json.obj("message" -> s"Credentials created successfully"))
        } else {
          InternalServerError(Json.obj("message" -> "Failed to create user"))
        }
      }
      .recover {
        case e: Exception =>
          InternalServerError(Json.obj("message" -> s"An error occurred: ${e.getMessage}"))
      }
  }

  def readCredentials(id: Long): Action[AnyContent] = Action.async { implicit request =>
    credentialsService
      .readCredentials(id)
      .map { crd =>
        Ok(CredentialsDTO.encode(crd))
      }
      .recover {
        case _: NoSuchElementException =>
          NotFound(s"User not found with id: $id")
        case ex: Exception =>
          InternalServerError(s"An error occurred: ${ex.getMessage}")
      }
  }

  def updateCredentials(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val updatedCredentials = CredentialsDTO.decode(request.body.asJson.get.toString)
    credentialsService
      .updateCredentials(id, updatedCredentials)
      .map { success =>
        if (success) {
          Ok("Credentials updated successfully")
        } else {
          NotFound("User not found")
        }
      }
      .recover {
        case ex: Exception =>
          InternalServerError(s"An error occurred: ${ex.getMessage}")
      }
  }

  def deleteCredentials(id: Long): Action[AnyContent] = Action.async { implicit request =>
    credentialsService
      .deleteCredentials(id)
      .map { success =>
        if (success) {
          Ok("Credentials deleted successfully")
        } else {
          NotFound("User not found")
        }
      }
      .recover {
        case ex: Exception =>
          InternalServerError(s"An error occurred: ${ex.getMessage}")
      }
  }

}
