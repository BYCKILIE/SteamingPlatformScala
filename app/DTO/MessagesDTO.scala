package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class MessagesDTO(data: Option[String] = None, senderId: Long, receiverId: Option[Long] = None, liveStreamId: Option[Long] = None, postingDate: Option[String] = None)

object MessagesDTO {
  implicit val messagesDecoder: Decoder[MessagesDTO] = deriveDecoder[MessagesDTO]
  implicit val messagesEncoder: Encoder[MessagesDTO] = deriveEncoder[MessagesDTO]

  def decode(jsonString: String): MessagesDTO = {
    parser.decode[MessagesDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: MessagesDTO): String = {
    dtoObject.asJson.noSpaces
  }
}