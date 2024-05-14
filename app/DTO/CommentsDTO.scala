package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class CommentsDTO(data: Option[String] = None, userId: Long, postId: Option[Long] = None, applicationPostId: Option[Long] = None, liveStreamId: Option[Long] = None, postingDate: Option[String] = None)

object CommentsDTO {
  implicit val commentsDecoder: Decoder[CommentsDTO] = deriveDecoder[CommentsDTO]
  implicit val commentsEncoder: Encoder[CommentsDTO] = deriveEncoder[CommentsDTO]

  def decode(jsonString: String): CommentsDTO = {
    parser.decode[CommentsDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: CommentsDTO): String = {
    dtoObject.asJson.noSpaces
  }
}