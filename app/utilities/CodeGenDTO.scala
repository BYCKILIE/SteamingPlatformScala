package utilities

import java.io.{File, PrintWriter}
import scala.io.Source

object CodeGenDTO {

  private def getClassNames(pck: String): Seq[String] = {
    val dirPath = "app/" + pck + "/"
    val dir     = new File(dirPath)
    if (dir.exists && dir.isDirectory) {
      dir.listFiles.filter(_.isFile).flatMap { file =>
        val fileName = file.getName
        if (fileName.endsWith(".scala")) {
          Some(s"app/$pck/$fileName")
        } else {
          None
        }
      }
    } else {
      Seq.empty[String]
    }
  }

  private def createFile(src: String, pck: String): Unit = {
    val originalClassFile    = src
    val source               = Source.fromFile(originalClassFile)
    val originalClassContent = source.mkString
    source.close()

    val classPattern  = "case class (\\w+)\\((.+)\\)".r
    val namePattern   = "(\\w+)\\(".r
    val fieldsPattern = "\\((.+)\\)".r

    val fullRow   = classPattern.findFirstIn(originalClassContent).getOrElse("")
    val className = namePattern.findFirstIn(fullRow).getOrElse("").dropRight(4) + "DTO"
    val fields    = fieldsPattern.findFirstIn(fullRow).getOrElse("")

    if ((className != "DTO") && fields.nonEmpty) {
      val newClassFile = s"$className.scala"
      val writer       = new PrintWriter(s"app/$pck/$newClassFile")
      writer.write(
        s"""package DTO\n
           |import io.circe._
           |import io.circe.generic.semiauto._
           |import io.circe.syntax.EncoderOps
           |
           |case class $className$fields\n
           |object $className {
           |  implicit val ${className.dropRight(3).head.toLower + className
            .dropRight(3)
            .tail}Decoder: Decoder[$className] = deriveDecoder[$className]
           |  implicit val ${className.dropRight(3).head.toLower + className
            .dropRight(3)
            .tail}Encoder: Encoder[$className] = deriveEncoder[$className]
           |
           |  def decode(jsonString: String): $className = {
           |    parser.decode[$className](jsonString) match {
           |      case Left(_) =>
           |        throw new IllegalArgumentException(s"invalid JSON object")
           |      case Right(dtoObject) => dtoObject
           |    }
           |  }
           |
           |  def encode(dtoObject: $className): String = {
           |    dtoObject.asJson.noSpaces
           |  }
           |}""".stripMargin
      )
      writer.close()
    } else {
      println("Error parsing the file")
    }
  }

  def main(args: Array[String]): Unit = {
    val sources = getClassNames("models")
    sources.foreach(src => createFile(src, "DTO"))
  }
}
