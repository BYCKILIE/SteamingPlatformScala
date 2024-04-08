package utilities

import DTO.UsersDTO
import play.api.mvc.{AnyContent, Request}

object JsonOP {

  def serialize[T: upickle.default.ReadWriter](value: T): String = {
    upickle.default.write(value)
  }

  def deserialize[T: upickle.default.ReadWriter : Manifest](implicit request: Request[AnyContent]): T =
    upickle.default.read[T](request.body.asJson.get.toString())

}