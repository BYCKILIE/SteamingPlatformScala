package controllers

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.Operation
//import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import play.api.mvc.Cookie
import services.CredentialsService
import services.TokensService
import utilities.Encryptor
import DTO.CredentialsDTO

@Singleton
class AuthenticationController @Inject() (
    cc: ControllerComponents,
    credentialsService: CredentialsService,
    tokensService: TokensService,
//    mailerClient: MailerClient
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {

  @Operation(
    summary = "Authenticate user",
    description = "Validate user credentials and generate or refresh authentication token",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Token generated or refreshed"),
      new ApiResponse(responseCode = "400", description = "Invalid public key"),
      new ApiResponse(responseCode = "401", description = "Invalid credentials or token expired"),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def authenticate(): Action[AnyContent] = Action.async { implicit request =>
    credentialsService
      .validateCredentials(
        CredentialsDTO.decode(
          request.body.asJson.get.toString()
        )
      )
      .flatMap { idRole =>
        tokensService.hasToken(idRole._1).flatMap { success =>
          request.cookies.get("publicKey") match {
            case Some(publicKey) =>
              println(publicKey)
              if (success) {
                tokensService.refresh(idRole._1, idRole._2, Option.apply(publicKey.value)).map {
                  case Some(token) => Ok(token).withCookies(Cookie("token", token))
                  case None        => Unauthorized("Cannot Refresh Token")
                }
              } else {
                tokensService.createToken(idRole._1, idRole._2, Option.apply(publicKey.value)).map {
                  case Some(token) => Ok(token).withCookies(Cookie("token", token))
                  case None        => Unauthorized("Cannot Generate Token")
                }
              }
            case None => Future.successful(Unauthorized("Invalid publicKey"))
          }
        }
      }
      .recover {
        case _: NoSuchElementException =>
          Unauthorized("Invalid Credentials")
        case ex: Exception =>
          InternalServerError(s"An error occurred: ${ex.getMessage}")
      }
  }

  @Operation(
    summary = "Authorize token",
    description = "Authorize the provided token",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Token authorized"),
      new ApiResponse(responseCode = "401", description = "Token not found in cookies or failed to refresh token")
    )
  )
  def authorise(): Action[AnyContent] = Action.async { implicit request =>
    request.cookies.get("token") match {
      case Some(cookie) =>
        val token = cookie.value
        tokensService.parseToken(token).flatMap { tokenDTO =>
          tokensService.hasToken(tokenDTO.userId.getOrElse(0)).flatMap { success =>
            if (success) {
              request.cookies.get("publicKey") match {
                case Some(publicKey) =>
//                  tokensService.authoriseToken(tokenDTO.token.getOrElse(""), Option.apply(publicKey.value)).flatMap {
//                    case Some(_) => Future.successful(Ok("Token not found in cookies"))
//                    case None    => Future.successful(Unauthorized("Failed to authorise token"))
//                  }
                  Future.successful(Ok("Token Authorised"))
                case None =>
                  Future.successful(Unauthorized("Failed to refresh token"))
              }
            } else {
              Future.successful(Unauthorized("Token expired"))
            }
          }
        }
      case None =>
        Future.successful(Unauthorized("Token not found in cookies"))
    }
  }

  @Operation(
    summary = "Get server public key",
    description = "Retrieve the server's public key for encrypting",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Server public key retrieved")
    )
  )
  def sendPublicKey(): Action[AnyContent] = Action { implicit request =>
    Ok(utilities.Encryptor.serverKey.getPublic.getFormat)
  }

//  def sendMail(): Action[AnyContent] = Action { implicit request =>
//    mailerClient.send(Email(
//      "Subject",
//      "tudor.tdr.7@gmail.com",
//      Seq("tudorovidiub@gmail.com"),
//      bodyText = Some("Text content"),
//    ))
//    Ok("email sent")
//  }

}
