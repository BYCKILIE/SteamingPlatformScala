package controllers

import actors.livestream.{LivestreamManagerActor, LivestreamClientActor, StreamerActor}
import javax.inject._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import akka.util.ByteString

@Singleton
class LivestreamController @Inject() (cc: ControllerComponents)(
  implicit system: ActorSystem,
  mat: Materializer,
) extends AbstractController(cc) {

  private val livestreamActor: ActorRef = system.actorOf(LivestreamManagerActor.props)

  def joinLivestream(streamId: Long): WebSocket = WebSocket.accept[ByteString, ByteString] { _ =>
    ActorFlow.actorRef { out =>
      LivestreamClientActor.props(streamId, out, livestreamActor)
    }
  }

  def stream(streamId: Long): WebSocket = WebSocket.accept[ByteString, ByteString] { _ =>
    ActorFlow.actorRef{ out =>
      StreamerActor.props(streamId, out, livestreamActor)
    }
  }
}
