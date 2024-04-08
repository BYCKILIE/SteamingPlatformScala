package repositories

import DTO.UsersDTO
import scala.concurrent.Future

trait UsersRepository {
  def createUser(user: UsersDTO): Future[Long]
  def readUser(id: Long): Future[UsersDTO]
  def updateUser(id: Long, updatedUser: UsersDTO): Future[Boolean]
  def deleteUser(id: Long): Future[Boolean]
}