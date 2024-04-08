package DTO

import upickle.default.{ReadWriter, macroRW}

case class ApplicationPostDTO(id: Long, title: Option[String] = None, description: Option[String] = None, data: Array[Byte], views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), postingDate: Option[String] = None)

object ApplicationPostDTO {
  implicit val applicationPostRW: ReadWriter[ApplicationPostDTO] = macroRW
}