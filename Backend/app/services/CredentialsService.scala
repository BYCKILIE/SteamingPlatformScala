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

@Singleton
class CredentialsService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ex: ExecutionContext
) extends CredentialsRepository {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def validateCredentials(credentials: CredentialsDTO): Future[Long] = {
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
      .map(_.get.userId)
  }

  private def validateUsername(username: String): Future[Seq[Credentials#TableElementType]] = {
    db.run(Credentials.filter(credentialsRow => credentialsRow.username === username).result)
  }

  private def validateEmail(email: String): Future[Seq[Credentials#TableElementType]] = {
    db.run(Credentials.filter(credentialsRow => credentialsRow.email === email).result)
  }

  def createCredentials(credentials: CredentialsDTO): Future[Boolean] = {
    db.run(
      Credentials += CredentialsRow(
        credentials.username,
        credentials.email,
        BCrypt.hashpw(credentials.password, BCrypt.gensalt()),
        credentials.userId
      )
    ).map(_ > 0)
  }

  def readCredentials(id: Long): Future[CredentialsDTO] = {
    db.run(Credentials.filter(_.userId === id).result.headOption).flatMap {
      case Some(crd) =>
        Future.successful(CredentialsDTO(crd.username, crd.email, "NULL", 0))
      case None =>
        Future.failed(new NoSuchElementException(s"Credentials with id $id not found"))
    }
  }

  def updateCredentials(id: Long, updatedCredentials: CredentialsDTO): Future[Boolean] = {
    db.run(Credentials.filter(_.userId === id).result.headOption).flatMap {
      case Some(existingCredentials) =>
        val updated = existingCredentials.copy(
          username = updatedCredentials.username,
          email = updatedCredentials.email,
          password = BCrypt.hashpw(updatedCredentials.password, BCrypt.gensalt())
        )
        db.run(Credentials.filter(_.userId === id).update(updated)).map(_ > 0)
      case None =>
        Future.successful(false) // User not found
    }
  }

  def deleteCredentials(id: Long): Future[Boolean] = {
    db.run(Credentials.filter(_.userId === id).delete).map(_ > 0)
  }

}
