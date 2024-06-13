package actors.livestream

import actors.livestream.LivestreamManagerActor.{Join, Leave}
import akka.actor.{Actor, ActorRef, Props}

class LivestreamClientActor(streamId: Long, out: ActorRef, manager: ActorRef) extends Actor {

  override def preStart(): Unit = {
    manager ! Join(streamId, out)
  }

  override def postStop(): Unit = {
    manager ! Leave(streamId, out)
  }

  def receive: Receive = {
    case _ =>
  }

}

object LivestreamClientActor {
  def props(streamId: Long, out: ActorRef, manager: ActorRef): Props = Props(new LivestreamClientActor(streamId, out, manager))
}