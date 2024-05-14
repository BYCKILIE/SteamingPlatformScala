package repositories

import DTO.MessagesDTO
import scala.concurrent.Future

trait MessagesRepository {
  def createMessage(message: MessagesDTO): Future[Boolean]
  def readMessage(implicit script: MessagesDTO): Future[MessagesDTO]
  def updateMessage(implicit updatedMessage: MessagesDTO): Future[Boolean]
  def deleteMessage(script: MessagesDTO): Future[Boolean]
}
