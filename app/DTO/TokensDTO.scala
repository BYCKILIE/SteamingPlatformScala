package DTO

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps

case class TokensDTO(userId: Option[Long] = None, role: Option[String], token: Option[String] = None)

object TokensDTO {
  implicit val tokensDecoder: Decoder[TokensDTO] = deriveDecoder[TokensDTO]
  implicit val tokensEncoder: Encoder[TokensDTO] = deriveEncoder[TokensDTO]

  def decode(jsonString: String): TokensDTO = {
    parser.decode[TokensDTO](jsonString) match {
      case Left(_) =>
        throw new IllegalArgumentException(s"invalid JSON object")
      case Right(dtoObject) => dtoObject
    }
  }

  def encode(dtoObject: TokensDTO): String = {
    dtoObject.asJson.noSpaces
  }
}