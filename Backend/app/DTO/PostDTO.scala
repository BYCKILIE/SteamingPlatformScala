package DTO

import upickle.default.{ReadWriter, macroRW}

case class PostDTO(id: Long, data: Array[Byte], views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), userId: Long, title: Option[String] = None, description: Option[String] = None, postingDate: Option[String] = None)

object PostDTO {
  implicit val postRW: ReadWriter[PostDTO] = macroRW
}