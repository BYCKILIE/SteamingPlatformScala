package DTO

import upickle.default.{ReadWriter, macroRW}

case class MessagesDTO(data: Option[String] = None, senderId: Long, receiverId: Option[Long] = None, liveStreamId: Option[Long] = None, postingDate: Option[String] = None)

object MessagesDTO {
  implicit val messagesRW: ReadWriter[MessagesDTO] = macroRW
}