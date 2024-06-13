package controllers

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import play.api.libs.json.Json

import java.util.Base64
//import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import services.CredentialsService
import services.TokensService
import utils.Encryptor
import utils.JsonOP
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
    val reqJson = JsonOP.parseString(request.body.asJson.getOrElse("").toString)
    val cKey    = JsonOP.getField(reqJson, "pKey")
    println(s"cKey: $cKey")
    println(s"us: ${JsonOP.getField(reqJson, "username")}")
    credentialsService
      .validateCredentials(
        CredentialsDTO.decode(
          request.body.asJson.get.toString()
        )
      )
      .flatMap { idRole =>
        tokensService.hasToken(idRole._1).flatMap {
          case Some(usedToken) =>
            tokensService.refresh(usedToken, idRole._1, idRole._2, Option.apply(cKey)).map {
              case Some(token) => Ok(token)
              case None        => Unauthorized("Cannot Refresh Token")
            }
          case None =>
            tokensService.createToken(idRole._1, idRole._2, Option.apply(cKey)).map {
              case Some(token) => Ok(token)
              case None        => Unauthorized("Cannot Generate Token")
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
    request.headers.get("Authorization") match {
      case Some(token) =>
        tokensService.parseToken(token).flatMap { tokenDTO =>
          tokensService.hasToken(tokenDTO.userId.getOrElse(0)).flatMap {
            case Some(usedToken) =>
              request.cookies.get("publicKey") match {
                case Some(publicKey) =>
                  tokensService.authoriseToken(usedToken, Option.apply(publicKey.value)).map {
                    case Some(tkn) => Ok(s"{token: $tkn, pKey: ${Encryptor.serverKey.getPublic.toString}}")
                    case None      => Unauthorized("Failed to authorise token")
                  }
                case None =>
                  Future.successful(Unauthorized("Failed to refresh token"))
              }
            case None => Future.successful(Unauthorized("Failed to extract token"))
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
  def sendPublicKey(): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(Base64.getEncoder.encodeToString(utils.Encryptor.serverKey.getPublic.getEncoded)))
  }

  def getConnected: Action[AnyContent] = Action.async { implicit request =>
    tokensService.getLoggedIn.flatMap { count =>
      Future.successful(Ok(Json.stringify(Json.obj("connectedCount" -> count))))
    }
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
