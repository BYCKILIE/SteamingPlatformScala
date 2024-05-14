package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class CredentialsDTO(username: String, email: String, password: String, userId: Long, role: Option[String] = Some("normal"))

object CredentialsDTO {
  implicit val credentialsDecoder: Decoder[CredentialsDTO] = deriveDecoder[CredentialsDTO]
  implicit val credentialsEncoder: Encoder[CredentialsDTO] = deriveEncoder[CredentialsDTO]

  def decode(jsonString: String): CredentialsDTO = {
    parser.decode[CredentialsDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: CredentialsDTO): String = {
    dtoObject.asJson.noSpaces
  }
}