package services

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import models.Tables.Credentials
import models.Tables.CredentialsRow
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.DatabaseConfigProvider
import repositories.CredentialsRepository
import slick.jdbc.JdbcProfile
import DTO.CredentialsDTO

/**
 * Service class responsible for handling operations related to user credentials.
 *
 * @param dbConfigProvider The database configuration provider.
 * @param ex               The execution context.
 */
@Singleton
class CredentialsService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(
  implicit ex: ExecutionContext
) extends CredentialsRepository {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  /**
   * Validates the provided credentials.
   *
   * @param credentials The credentials to validate.
   * @return A future containing the user ID and role if the credentials are valid.
   */
  override def validateCredentials(credentials: CredentialsDTO): Future[(Long, Option[String])] = {
    val matches = (credentials.username, credentials.email) match {
      case (_, "NULL") => validateUsername(credentials.username)
      case _           => validateEmail(credentials.email)
    }
    matches
      .map { credentialRow =>
        credentialRow.find { credentialRow =>
          BCrypt.checkpw(credentials.password, credentialRow.password)
        }
      }
      .map(user => (user.get.userId, user.get.role))
  }

  /**
   * Validates a username.
   *
   * @param username The username to validate.
   * @return A future containing the credentials associated with the username.
   */
  private def validateUsername(username: String): Future[Seq[Credentials#TableElementType]] = {
    db.run(Credentials.filter(credentialsRow => credentialsRow.username === username).result)
  }

  /**
   * Validates an email address.
   *
   * @param email The email address to validate.
   * @return A future containing the credentials associated with the email address.
   */
  private def validateEmail(email: String): Future[Seq[Credentials#TableElementType]] = {
    db.run(Credentials.filter(credentialsRow => credentialsRow.email === email).result)
  }

  /**
   * Creates new credentials.
   *
   * @param credentials The credentials to create.
   * @return A future indicating whether the creation was successful.
   */
  override def createCredentials(credentials: CredentialsDTO): Future[Boolean] = {
    db.run(
      Credentials += CredentialsRow(
        credentials.username.toLowerCase,
        credentials.email.toLowerCase,
        BCrypt.hashpw(credentials.password, BCrypt.gensalt()),
        credentials.userId
      )
    ).map(_ > 0)
  }

  /**
   * Reads credentials associated with a user ID.
   *
   * @param id The user ID.
   * @return A future containing the credentials associated with the user ID.
   */
  override def readCredentials(id: Long): Future[CredentialsDTO] = {
    db.run(Credentials.filter(_.userId === id).result.headOption).flatMap {
      case Some(crd) =>
        Future.successful(CredentialsDTO(crd.username, crd.email, "NULL", 0, crd.role))
      case None =>
        Future.failed(new NoSuchElementException(s"Credentials with id $id not found"))
    }
  }

  /**
   * Updates existing credentials.
   *
   * @param id                The user ID associated with the credentials to update.
   * @param updatedCredentials The updated credentials.
   * @return A future indicating whether the update was successful.
   */
  override def updateCredentials(id: Long, updatedCredentials: CredentialsDTO): Future[Boolean] = {
    db.run(Credentials.filter(_.userId === id).result.headOption).flatMap {
      case Some(existingCredentials) =>
        val updated = existingCredentials.copy(
          username = updatedCredentials.username.toLowerCase,
          email = updatedCredentials.email.toLowerCase,
          password = BCrypt.hashpw(updatedCredentials.password, BCrypt.gensalt()),
          role = updatedCredentials.role
        )
        db.run(Credentials.filter(_.userId === id).update(updated)).map(_ > 0)
      case None =>
        Future.successful(false) // User not found
    }
  }

  /**
   * Deletes credentials associated with a user ID.
   *
   * @param id The user ID.
   * @return A future indicating whether the deletion was successful.
   */
  override def deleteCredentials(id: Long): Future[Boolean] = {
    db.run(Credentials.filter(_.userId === id).delete).map(_ > 0)
  }

  def isRegistered(email: String): Future[Boolean] = {
    db.run(Credentials.filter(_.email === email).result.headOption.map(_.isDefined))
  }

}
