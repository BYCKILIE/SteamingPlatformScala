package controllers

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import actors.WebSocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer

@Singleton
class WebSocketController @Inject() (cc: ControllerComponents)(
    implicit system: ActorSystem,
    mat: Materializer,
    ec: ExecutionContext
) extends AbstractController(cc) {

  def socket(id: String): WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      WebSocketActor.props(out, id)
    }
  }
}
