package services

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import models.Tables._
import play.api.db.slick.DatabaseConfigProvider
import repositories.UsersRepository
import slick.jdbc.JdbcProfile
import DTO.UsersDTO

@Singleton
class UsersService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ex: ExecutionContext)
    extends UsersRepository {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  override def createUser(user: UsersDTO): Future[Long] = {
    val insertAction =
      (Users.returning(Users.map(_.id))) += UsersRow(0, user.firstName, user.lastName, user.birthDate, user.gender)
    db.run(insertAction)
  }

  def readUser(id: Long): Future[UsersDTO] = {
    db.run(Users.filter(_.id === id).result.headOption).flatMap {
      case Some(ud) =>
        Future.successful(UsersDTO(ud.id, ud.firstName, ud.lastName, ud.birthDate, ud.gender))
      case None =>
        Future.failed(new NoSuchElementException(s"User with id $id not found"))
    }
  }

  def updateUser(id: Long, updatedUser: UsersDTO): Future[Boolean] = {
    db.run(Users.filter(_.id === id).result.headOption).flatMap {
      case Some(existingUser) =>
        val updated = existingUser.copy(
          firstName = updatedUser.firstName,
          lastName = updatedUser.lastName,
          birthDate = updatedUser.birthDate,
          gender = updatedUser.gender
        )
        db.run(Users.filter(_.id === id).update(updated)).map(_ > 0)
      case None =>
        Future.successful(false) // User not found
    }
  }

  def deleteUser(id: Long): Future[Boolean] = {
    db.run((for {
      _       <- Credentials.filter(_.userId === id).delete
      deleted <- Users.filter(_.id === id).delete
    } yield deleted > 0).transactionally)
  }

}
