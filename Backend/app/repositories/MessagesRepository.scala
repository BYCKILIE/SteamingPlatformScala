package repositories

import DTO.MessagesDTO
import scala.concurrent.Future

trait MessagesRepository {
  def createMessage(user: MessagesDTO): Future[Boolean]
  def readMessage(script: MessagesDTO): Future[MessagesDTO]
  def updateMessage(updatedMessage: MessagesDTO): Future[Boolean]
  def deleteMessage(script: MessagesDTO): Future[Boolean]
}
