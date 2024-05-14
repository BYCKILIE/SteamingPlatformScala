package utilities

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

}