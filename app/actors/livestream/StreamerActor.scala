package actors.livestream

import actors.livestream.LivestreamManagerActor.Broadcast
import akka.actor.{Actor, ActorRef, Props}
import akka.util.ByteString

class StreamerActor(streamId: Long, out: ActorRef, manager: ActorRef) extends Actor {

  def receive: Receive = {
    case frame: ByteString =>
      manager ! Broadcast(streamId, frame)
      out ! ByteString.emptyByteString
  }

}

object StreamerActor {
  def props(streamId: Long, out: ActorRef, manager: ActorRef): Props = Props(new StreamerActor(streamId, out, manager))
}