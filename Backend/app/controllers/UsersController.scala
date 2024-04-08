package controllers

import DTO.UsersDTO
import utilities.JsonOP

import javax.inject._

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Format.GenericFormat

import scala.concurrent.ExecutionContext

@Singleton
class UsersController @Inject()(cc: ControllerComponents, userService: services.UsersService)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def createUser(): Action[AnyContent] = Action.async { implicit request =>
    userService.createUser(JsonOP.deserialize[UsersDTO]).map { success =>
      if (success > 0) {
        Ok(Json.obj("message" -> s"User created successfully at $success id"))
      } else {
        InternalServerError(Json.obj("message" -> "Failed to create user"))
      }
    }.recover {
      case e: Exception =>
        InternalServerError(Json.obj("message" -> s"An error occurred: ${e.getMessage}"))
    }
  }

  def readUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
    userService.readUser(id).map { user =>
      Ok(JsonOP.serialize(user))
    }.recover {
      case _: NoSuchElementException =>
        NotFound(s"User not found with id: $id")
      case ex: Exception =>
        InternalServerError(s"An error occurred: ${ex.getMessage}")
    }
  }

  def updateUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val updatedUser = JsonOP.deserialize[UsersDTO]
    userService.updateUser(id, updatedUser).map { success =>
      if (success) {
        Ok("User updated successfully")
      } else {
        NotFound("User not found")
      }
    }.recover {
      case ex: Exception =>
        InternalServerError(s"An error occurred: ${ex.getMessage}")
    }
  }

  def deleteUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
    userService.deleteUser(id).map { success =>
      if (success) {
        Ok("User deleted successfully")
      } else {
        NotFound("User not found")
      }
    }.recover {
      case ex: Exception =>
        InternalServerError(s"An error occurred: ${ex.getMessage}")
    }
  }

}
