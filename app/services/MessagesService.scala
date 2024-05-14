package services

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import models.Tables._
import play.api.db.slick.DatabaseConfigProvider
import repositories.MessagesRepository
import slick.jdbc.JdbcProfile
import DTO.MessagesDTO

@Singleton
class MessagesService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ex: ExecutionContext)
    extends MessagesRepository {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  override def createMessage(message: MessagesDTO): Future[Boolean] = {
    db.run(
      Messages += MessagesRow(
        message.data,
        message.senderId,
        message.receiverId,
        message.liveStreamId,
        message.postingDate
      )
    ).map(_ > 0)
  }

  private def queryMessage(implicit script: MessagesDTO): Future[Option[MessagesRow]] = {
    (script.receiverId, script.liveStreamId) match {
      case (_, None) =>
        db.run(
          Messages
            .filter(msg => msg.senderId === script.senderId && msg.receiverId === script.receiverId)
            .result
            .headOption
        )
      case _ =>
        db.run(
          Messages
            .filter(msg => msg.senderId === script.senderId && msg.liveStreamId === script.liveStreamId)
            .result
            .headOption
        )
    }
  }

  def readMessage(implicit script: MessagesDTO): Future[MessagesDTO] = {
    queryMessage.flatMap {
      case Some(msg) =>
        Future.successful(MessagesDTO(msg.data, msg.senderId, msg.receiverId, msg.liveStreamId, msg.postingDate))
      case None =>
        Future.failed(new NoSuchElementException("Message not found"))
    }
  }

  def updateMessage(implicit updateMessage: MessagesDTO): Future[Boolean] = {
    queryMessage.flatMap {
      case Some(existingMessage) =>
        val updated = existingMessage.copy(
          data = updateMessage.data
        )
        (updated.receiverId, updated.liveStreamId) match {
          case (_, None) =>
            db.run(
              Messages
                .filter(msg => msg.senderId === updated.senderId && msg.receiverId === updated.receiverId)
                .update(updated)
            ).map(_ > 0)
          case _ =>
            db.run(
              Messages
                .filter(msg => msg.senderId === updated.senderId && msg.liveStreamId === updated.liveStreamId)
                .update(updated)
            ).map(_ > 0)
        }
      case None =>
        Future.successful(false)
    }
  }

  def deleteMessage(script: MessagesDTO): Future[Boolean] = {
    (script.receiverId, script.liveStreamId) match {
      case (_, None) =>
        db.run(Messages.filter(msg => msg.senderId === script.senderId && msg.receiverId === script.receiverId).delete)
          .map(_ > 0)
      case _ =>
        db.run(
          Messages.filter(msg => msg.senderId === script.senderId && msg.liveStreamId === script.liveStreamId).delete
        ).map(_ > 0)
    }
  }

}
