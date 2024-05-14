package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class UsersDTO(id: Long, firstName: String, lastName: String, birthDate: String, gender: Int)

object UsersDTO {
  implicit val usersDecoder: Decoder[UsersDTO] = deriveDecoder[UsersDTO]
  implicit val usersEncoder: Encoder[UsersDTO] = deriveEncoder[UsersDTO]

  def decode(jsonString: String): UsersDTO = {
    parser.decode[UsersDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: UsersDTO): String = {
    dtoObject.asJson.noSpaces
  }
}