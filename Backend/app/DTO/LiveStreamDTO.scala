package DTO

import upickle.default.{ReadWriter, macroRW}

case class LiveStreamDTO(id: Long, title: Option[String] = None, description: Option[String] = None, data: Option[Array[Byte]] = None, views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), userId: Long, postingDate: Option[String] = None)

object LiveStreamDTO {
  implicit val liveStreamRW: ReadWriter[LiveStreamDTO] = macroRW
}