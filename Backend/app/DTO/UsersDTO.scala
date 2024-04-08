package DTO

import upickle.default.{ReadWriter, macroRW}

case class UsersDTO(id: Long, firstName: String, lastName: String, birthDate: String, gender: Int)

object UsersDTO {
  implicit val usersRW: ReadWriter[UsersDTO] = macroRW
}