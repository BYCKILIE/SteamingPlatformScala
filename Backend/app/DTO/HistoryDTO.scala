package DTO

import upickle.default.{ReadWriter, macroRW}

case class HistoryDTO(userId: Long, postId: Option[Long] = None, applicationPostId: Option[Long] = None, liveStreamId: Option[Long] = None, profileId: Option[Long] = None, postingDate: Option[String] = None)

object HistoryDTO {
  implicit val historyRW: ReadWriter[HistoryDTO] = macroRW
}