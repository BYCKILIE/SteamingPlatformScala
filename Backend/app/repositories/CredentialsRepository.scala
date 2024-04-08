package repositories

import DTO.CredentialsDTO
import scala.concurrent.Future

trait CredentialsRepository {
  def validateCredentials(credentials: CredentialsDTO): Future[Long]
  def createCredentials(credentials: CredentialsDTO): Future[Boolean]
  def readCredentials(id: Long): Future[CredentialsDTO]
  def updateCredentials(id: Long, updatedCredentials: CredentialsDTO): Future[Boolean]
  def deleteCredentials(id: Long): Future[Boolean]
}
