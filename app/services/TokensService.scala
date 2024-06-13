package services

import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import models.Tables.Tokens
import models.Tables.TokensRow
import pdi.jwt._
import play.api.db.slick.DatabaseConfigProvider
import repositories.TokensRepository
import slick.jdbc.JdbcProfile
import DTO.TokensDTO

/**
 * Service class responsible for handling operations related to authentication tokens.
 *
 * @param dbConfigProvider The database configuration provider.
 * @param ex               The execution context.
 */
@Singleton
class TokensService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ex: ExecutionContext)
    extends TokensRepository {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val secretKey = "serverKey"
  private val algo      = JwtAlgorithm.HS256

  val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

  private def getNowTimeString: String = {
    val now = LocalDateTime.now()
    now.format(dateFormatter)
  }

  /**
   * Generates a new authentication token.
   *
   * @param userId     The ID of the user associated with the token.
   * @param role       The role of the user.
   * @param publicKey  The public key.
   * @return A future containing the generated token.
   */
  def createToken(userId: Long, role: Option[String], publicKey: Option[String]): Future[Option[String]] = {
    val token = generateToken(userId, role, publicKey)
    db.run(
      Tokens += TokensRow(
        userId = Option(userId),
        token = Option.apply(token),
        createdAt = Option.apply(getNowTimeString)
      )
    ).map(success => if (success > 0) Option.apply(token) else Option.empty)
  }

  /**
   * Parses an authentication token.
   *
   * @param token The token to parse.
   * @return A future containing the parsed token information.
   */
  def parseToken(token: String): Future[TokensDTO] = {
    Jwt.decode(token, secretKey, Seq(algo)) match {
      case Success(claim) =>
        Future.apply(new TokensDTO(claim.jwtId.getOrElse("").toLongOption, claim.issuer))
      case Failure(_) => Future.failed(new NoSuchElementException(s"Token Incorrect"))
    }
  }

  /**
   * Authorizes an authentication token.
   *
   * @param token      The token to authorize.
   * @param publicKey  The public key.
   * @return A future containing the authorized token, if valid.
   */
  def authoriseToken(token: String, publicKey: Option[String]): Future[Option[String]] = {
    Jwt.decode(token, secretKey, Seq(algo)) match {
      case Success(claim) =>
        claim.expiration match {
          case Some(expiration) =>
            val now = Instant.now.getEpochSecond
            if (expiration > now) {
              refresh(token, claim.jwtId.getOrElse("").toLong, claim.issuer, publicKey).map {
                case Some(tkn) => Some(tkn)
                case None =>
                  None
              }
            } else {
              Future.successful(None)
            }
          case None =>
            Future.successful(None)
        }
      case Failure(_) =>
        Future.successful(None)
    }
  }

  /**
   * Checks if a user has a token.
   *
   * @param userId The ID of the user.
   * @return A future indicating whether the user has a token.
   */
  def hasToken(userId: Long): Future[Option[String]] = {
    db.run(Tokens.filter(tokensRow => tokensRow.userId === userId).result.headOption)
      .map {
        case Some(user) => user.token
        case None       => None
      }
  }

  /**
   * Refreshes an authentication token.
   *
   * @param userId     The ID of the user associated with the token.
   * @param role       The role of the user.
   * @param publicKey  The public key.
   * @return A future containing the refreshed token, if successful.
   */
  def refresh(
      usedToken: String,
      userId: Long,
      role: Option[String],
      publicKey: Option[String]
  ): Future[Option[String]] = {
    val token = generateToken(userId, role, publicKey)
    db.run(Tokens.filter(_.userId === userId).result.headOption).flatMap {
      case Some(existingToken) =>
        if (existingToken.token.getOrElse("") == usedToken) {
          val updated = existingToken.copy(
            token = Option.apply(token),
            createdAt = Option.apply(getNowTimeString)
          )
          db.run(Tokens.filter(_.userId === userId).update(updated)).map(_ > 0).flatMap { updatedRows =>
            if (updatedRows) Future.successful(Some(token))
            else Future.successful(None)
          }
        } else {
          Future.successful(None)
        }
      case None =>
        Future.successful(None)
    }
  }

  def getLoggedIn: Future[Int] = {
    val comparator = LocalDateTime.now().minusHours(1)
    db.run(Tokens.result).map { rows =>
      val validRows = rows.filter { row =>
        val rowDate = LocalDateTime.parse(row.createdAt.getOrElse(""), dateFormatter)
        comparator.isBefore(rowDate)
      }
      validRows.length
    }
  }

  /**
   * Generates a new authentication token.
   *
   * @param userId     The ID of the user associated with the token.
   * @param role       The role of the user.
   * @param publicKey  The public key.
   * @return The generated token.
   */
  private def generateToken(userId: Long, role: Option[String], publicKey: Option[String]): String = {
    val claim = JwtClaim(
      jwtId = Option(String.valueOf(userId)),
      issuer = role,
      subject = publicKey,
      expiration = Some(Instant.now.plusSeconds(2_592_000).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond)
    )
    Jwt.encode(claim, secretKey, algo)
  }
}
