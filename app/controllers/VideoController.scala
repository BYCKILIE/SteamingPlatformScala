// VideoController.scala

package controllers

import java.io.File
import java.nio.file.Paths
import javax.inject._

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import actors.VideoSenderActor
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.IOResult
import akka.stream.Materializer
import akka.util.ByteString
import play.api._
import play.api.http.HttpEntity
import play.api.libs.streams.ActorFlow
import play.api.mvc._

@Singleton
class VideoController @Inject() (cc: ControllerComponents)(
    implicit executionContext: ExecutionContext,
    implicit val system: ActorSystem,
    mat: Materializer,
) extends AbstractController(cc) {

  def streamVideo(): Action[AnyContent] = Action.async { request =>
    val file = Paths.get("posts_bank/test.mp4").toFile

    if (file.exists()) {
      val fileSource: Source[ByteString, Future[IOResult]] = FileIO.fromPath(file.toPath)

      val contentTypeHeader = "video/mp4"
      val contentLength     = file.length()

      val headers = Map(
        CONTENT_DISPOSITION -> s"""inline; filename="test.mp4"""",
      )

      Future.successful(
        Result(
          header = ResponseHeader(200, headers),
          body = HttpEntity.Streamed(fileSource, Some(contentLength), Some(contentTypeHeader))
        )
      )
    } else {
      Future.successful(
        NotFound("Video not found")
      )
    }
  }

  def receiveVideo(): WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      VideoSenderActor.props(out)
    }
  }
}
