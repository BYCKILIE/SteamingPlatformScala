package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class PostDTO(id: Long, views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), userId: Long = 0L, title: String, description: Option[String] = None, postingDate: String, path: String, postType: String, thumbnail: String)

object PostDTO {
  implicit val postDecoder: Decoder[PostDTO] = deriveDecoder[PostDTO]
  implicit val postEncoder: Encoder[PostDTO] = deriveEncoder[PostDTO]

  def decode(jsonString: String): PostDTO = {
    parser.decode[PostDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: PostDTO): String = {
    dtoObject.asJson.noSpaces
  }
}