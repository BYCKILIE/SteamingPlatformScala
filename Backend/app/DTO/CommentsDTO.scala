package DTO

import upickle.default.{ReadWriter, macroRW}

case class CommentsDTO(data: Option[String] = None, userId: Long, postId: Option[Long] = None, applicationPostId: Option[Long] = None, liveStreamId: Option[Long] = None, postingDate: Option[String] = None)

object CommentsDTO {
  implicit val commentsRW: ReadWriter[CommentsDTO] = macroRW
}