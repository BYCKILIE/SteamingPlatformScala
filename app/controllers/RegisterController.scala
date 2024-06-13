package controllers

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api.libs.json._
import play.api.mvc._
import services.{CredentialsService, RegisterService, UsersService}
import utils.JsonOP
import DTO.{CredentialsDTO, UsersDTO}

@Singleton
class RegisterController @Inject() (
    cc: ControllerComponents,
    userService: UsersService,
    credentialsService: CredentialsService,
    registerService: RegisterService
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {

  private val emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$".r
  private def isValid(email: String): Boolean = {
    email match {
      case emailRegex(_*) => true
      case _ => false
    }
  }

  def migrate: Action[AnyContent] = Action.async { implicit request =>
    val json = JsonOP.parseString(request.body.asJson.get.toString())

    val id       = JsonOP.getField(json, "id")
    val username = JsonOP.getField(json, "username")
    val password = JsonOP.getField(json, "password")

    registerService.readTemp(id).flatMap { user =>
      registerService.deleteTemp(id)

      userService.createUser(UsersDTO(0, user.firstName, user.lastName, user.birthDate, user.gender)).flatMap {
        userId =>
          if (userId > 0) {
            credentialsService
              .createCredentials(CredentialsDTO(username, user.email, password, userId, Option("normal")))
              .flatMap { success =>
                if (success) {
                  Future.successful(Ok(Json.obj("message" -> "User created")))
                } else {
                  userService.deleteUser(userId)
                  Future.successful(InternalServerError(Json.obj("message" -> "Failed to register user")))
                }
              }
          } else {
            Future.successful(InternalServerError(Json.obj("message" -> "Failed to create user")))
          }
      }
    }.recover {
      case _: NoSuchElementException => NotFound(Json.obj("error" -> "User not found"))
      case ex: Throwable             => InternalServerError(Json.obj("error" -> ex.getMessage))
    }
  }

  def completeTemp: Action[AnyContent] = Action.async { implicit request =>
    val json = JsonOP.parseString(request.body.asJson.get.toString())

    val id        = JsonOP.getField(json, "id")
    val firstName = JsonOP.getField(json, "firstName")
    val lastName  = JsonOP.getField(json, "lastName")
    val birthDate = JsonOP.getField(json, "birthDate")
    val gender    = JsonOP.getField(json, "gender").toInt

    val numerical = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
    if (firstName.contains(numerical) || lastName.contains(numerical)) {
      Future.successful(NotAcceptable)
    }

    registerService.completeTemp(id, firstName, lastName, birthDate, gender).map { success =>
      if (success) {
        Ok
      } else {
        InternalServerError
      }
    }
  }

  def isRegistered: Action[AnyContent] = Action.async { implicit request =>
    val json  = JsonOP.parseString(request.body.asJson.get.toString())
    val email = JsonOP.getField(json, "email")

    if (!isValid(email)) {
      Future.successful(NotAcceptable)
    }

    credentialsService.isRegistered(email).flatMap { success =>
      if (success) {
        Future.successful(Ok)
      } else {
        registerService.createTemp(email).flatMap { created =>
          if (created != "error") {
            Future.successful(Created(created))
          } else {
            Future.successful(InternalServerError)
          }
        }
      }
    }
  }

}
