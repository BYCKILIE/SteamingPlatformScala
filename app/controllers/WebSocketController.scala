package controllers

import actors.chat.{ChatManagerActor, ChatClientActor}
import javax.inject._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer

@Singleton
class WebSocketController @Inject() (cc: ControllerComponents)(
    implicit system: ActorSystem,
    mat: Materializer,
) extends AbstractController(cc) {

  private val chatActor: ActorRef = system.actorOf(ChatManagerActor.props)

  def chat(chatId: Long): WebSocket = WebSocket.accept[String, String] { _ =>
    ActorFlow.actorRef { out =>
      ChatClientActor.props(chatId, out, chatActor)
    }
  }
}
