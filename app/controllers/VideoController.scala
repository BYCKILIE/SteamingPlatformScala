// VideoController.scala

package controllers

import java.io.File
import java.nio.file.Files

import akka.stream.scaladsl._
import akka.util.ByteString
import javax.inject._
import play.api._
import play.api.http.HttpEntity
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class VideoController @Inject()(cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  def streamVideo(videoPath: String, chunk: String, video: Boolean): Action[AnyContent] = Action { implicit request =>
    val location = if (video) "videos" else "audios"
    val format = if (video) "avi" else "wav"

    val file = new File(s"posts_bank\\$location\\$videoPath\\chunk_$chunk.$format")
    if (file.exists()) {
      val source = FileIO.fromPath(file.toPath)
      val contentLength = file.length()
      val entity = HttpEntity.Streamed(source, Some(contentLength), Some(s"video/avi"))
      Result(
        header = ResponseHeader(200),
        body = entity
      )
    } else {
      NotFound("Video not found")
    }
  }
}
