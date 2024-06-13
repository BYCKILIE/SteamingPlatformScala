package controllers

import java.nio.file.Paths
import javax.inject._

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.Elem

import actors.VideoSenderActor
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.IOResult
import akka.stream.Materializer
import akka.util.ByteString
import play.api.http.HttpEntity
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import services.CredentialsService
import services.PostService
import DTO.PostDTO

import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, Sink, Source}

@Singleton
class VideoController @Inject() (cc: ControllerComponents)(
    implicit executionContext: ExecutionContext,
    implicit val system: ActorSystem,
    mat: Materializer,
    postService: PostService,
    credentialsService: CredentialsService
) extends AbstractController(cc) {

  def listVideos: Action[AnyContent] = Action.async {
    postService.listPosts.flatMap { posts =>
      val futureXmlElements: Future[Seq[Elem]] = Future.sequence(posts.map(videoToXml))
      futureXmlElements.map { xmlElements =>
        val xmlResponse: Elem = <videos>{xmlElements}</videos>
        Ok(xmlResponse)
      }
    }
  }

  def uploadVideo: Action[AnyContent] = Action.async { implicit request =>
    postService.createPost(PostDTO.decode(request.body.asJson.getOrElse("").toString)).map { success =>
      Ok(success.toString)
    }
  }

  private def videoToXml(post: PostDTO): Future[Elem] = {
    credentialsService.readCredentials(post.userId).map { userData =>
      <video>
        <id>{post.id}</id>
        <userId>{userData.username}</userId>
        <views>{post.views.getOrElse(0)}</views>
        <title>{post.title}</title>
        <postingDate>{post.postingDate}</postingDate>
        <thumbnail>{post.thumbnail}</thumbnail>
        <type>{post.postType}</type>
      </video>
    }
  }

  def streamVideo(videoId: Long): Action[AnyContent] = Action.async { _ =>
    postService.readPost(videoId).flatMap { post =>
      val file = Paths.get(s"posts_bank/${post.path}").toFile

      if (file.exists()) {
        val fileSource: Source[ByteString, Future[IOResult]] = FileIO.fromPath(file.toPath)

        val contentTypeHeader = s"video/${extractVideoType(post.path)}"
        val contentLength     = file.length()

        val headers = Map(
          CONTENT_DISPOSITION -> s"""inline; filename=${post.path}""",
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
  }

  private def extractVideoType(path: String): String = {
    val lastDotIndex = path.lastIndexOf('.')
    if (lastDotIndex != -1) path.substring(lastDotIndex + 1) else "*"
  }

  def receiveVideo(): WebSocket = WebSocket.accept[String, String] { _ =>
    ActorFlow.actorRef { out =>
      VideoSenderActor.props(out)
    }
  }
}
