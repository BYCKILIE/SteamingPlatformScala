package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class LiveStreamDTO(id: Long, title: Option[String] = None, description: Option[String] = None, views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), userId: Long, postingDate: Option[String] = None, path: String)

object LiveStreamDTO {
  implicit val liveStreamDecoder: Decoder[LiveStreamDTO] = deriveDecoder[LiveStreamDTO]
  implicit val liveStreamEncoder: Encoder[LiveStreamDTO] = deriveEncoder[LiveStreamDTO]

  def decode(jsonString: String): LiveStreamDTO = {
    parser.decode[LiveStreamDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: LiveStreamDTO): String = {
    dtoObject.asJson.noSpaces
  }
}