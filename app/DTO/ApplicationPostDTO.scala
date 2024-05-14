package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class ApplicationPostDTO(id: Long, title: Option[String] = None, description: Option[String] = None, views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), postingDate: Option[String] = None, path: String)

object ApplicationPostDTO {
  implicit val applicationPostDecoder: Decoder[ApplicationPostDTO] = deriveDecoder[ApplicationPostDTO]
  implicit val applicationPostEncoder: Encoder[ApplicationPostDTO] = deriveEncoder[ApplicationPostDTO]

  def decode(jsonString: String): ApplicationPostDTO = {
    parser.decode[ApplicationPostDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: ApplicationPostDTO): String = {
    dtoObject.asJson.noSpaces
  }
}