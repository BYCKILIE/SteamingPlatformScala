package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class HistoryDTO(userId: Long, postId: Option[Long] = None, profileId: Option[Long] = None, viewingDate: Option[String] = None)

object HistoryDTO {
  implicit val historyDecoder: Decoder[HistoryDTO] = deriveDecoder[HistoryDTO]
  implicit val historyEncoder: Encoder[HistoryDTO] = deriveEncoder[HistoryDTO]

  def decode(jsonString: String): HistoryDTO = {
    parser.decode[HistoryDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: HistoryDTO): String = {
    dtoObject.asJson.noSpaces
  }
}