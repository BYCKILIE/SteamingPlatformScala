package utils

import io.circe._
import io.circe.parser._

object JsonOP {

  def parseString(data: String): Json = {
    parse(data) match {
      case Left(parsingError) =>
        throw new IllegalArgumentException(s"Invalid JSON object: ${parsingError.message}")
      case Right(json) => json
    }
  }

  def getField(json: Json, fieldName: String): String = {
    json.asObject.flatMap(_.apply(fieldName)).flatMap(_.asString).getOrElse("")
  }

}