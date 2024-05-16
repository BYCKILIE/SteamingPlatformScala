package controllers

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api.libs.json._
import play.api.libs.json.Format.GenericFormat
import play.api.mvc._
import utilities.JsonOP
import services.{CredentialsService, UsersService}
import DTO.{CredentialsDTO, UsersDTO}

@Singleton
class RegisterController @Inject() (cc: ControllerComponents,
                                    userService: UsersService,
                                    credentialsService: CredentialsService)(
  implicit ec: ExecutionContext
) extends AbstractController(cc) {

  def register: Action[AnyContent] = Action.async { implicit request =>
    val json = JsonOP.parseString(request.body.asJson.get.toString())

    val username = JsonOP.getField(json, "username")
    val email = JsonOP.getField(json, "email")
    val password = JsonOP.getField(json, "password")
    val firstName = JsonOP.getField(json, "firstName")
    val lastName = JsonOP.getField(json, "lastName")
    val birthDate = JsonOP.getField(json, "birthDate")
    val gender = JsonOP.getField(json, "gender")

    println(username)
    println(email)
    println(password)
    println(firstName)
    println(lastName)
    println(birthDate)
    println(gender)

    val userCreationResult = userService.createUser(UsersDTO(0, firstName, lastName, birthDate, gender.toInt))
    val credentialsCreationResult = userCreationResult.flatMap { userId =>
      credentialsService.createCredentials(CredentialsDTO(username, email, password, userId, Option("normal")))
    }
    credentialsCreationResult.map { success =>
      if (success) {
        Ok(Json.obj("message" -> "Credentials created successfully"))
      } else {
        InternalServerError(Json.obj("message" -> "Failed to create user"))
      }
    }.recover {
      case e: Exception =>
        InternalServerError(Json.obj("message" -> s"An error occurred: ${e.getMessage}"))
    }
  }

  def isRegistered: Action[AnyContent] = Action.async { implicit request =>
    val json = JsonOP.parseString(request.body.asJson.get.toString())
    val email = JsonOP.getField(json, "email")
    credentialsService.isRegistered(email).map { success =>
      if (success) {
        Ok
      } else {
        NotFound
      }
    }
  }

  def createUser(): Action[AnyContent] = Action.async { implicit request =>
    userService
      .createUser(UsersDTO.decode(request.body.asJson.get.toString()))
      .map { success =>
        if (success > 0) {
          Ok(Json.obj("message" -> s"User created successfully at $success id"))
        } else {
          InternalServerError(Json.obj("message" -> "Failed to create user"))
        }
      }
      .recover {
        case e: Exception =>
          InternalServerError(Json.obj("message" -> s"An error occurred: ${e.getMessage}"))
      }
  }

  def readUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
    userService
      .readUser(id)
      .map { user =>
        Ok(UsersDTO.encode(user))
      }
      .recover {
        case _: NoSuchElementException =>
          NotFound(s"User not found with id: $id")
        case ex: Exception =>
          InternalServerError(s"An error occurred: ${ex.getMessage}")
      }
  }

  def updateUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val updatedUser = UsersDTO.decode(request.body.asJson.get.toString())
    userService
      .updateUser(id, updatedUser)
      .map { success =>
        if (success) {
          Ok("User updated successfully")
        } else {
          NotFound("User not found")
        }
      }
      .recover {
        case ex: Exception =>
          InternalServerError(s"An error occurred: ${ex.getMessage}")
      }
  }

  def deleteUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
    userService
      .deleteUser(id)
      .map { success =>
        if (success) {
          Ok("User deleted successfully")
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
