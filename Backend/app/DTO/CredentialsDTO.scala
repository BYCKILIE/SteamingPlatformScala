package DTO

import upickle.default.{ReadWriter, macroRW}

case class CredentialsDTO(username: String, email: String, password: String, userId: Long, role: Option[String] = Some("normal"))

object CredentialsDTO {
  implicit val credentialsRW: ReadWriter[CredentialsDTO] = macroRW
}