package actors.livestream

import actors.livestream.LivestreamManagerActor.{Broadcast, Join, Leave}
import akka.actor._
import akka.util.ByteString

import scala.collection.mutable

class LivestreamManagerActor extends Actor {
  private var clients = new mutable.HashMap[Long, Set[ActorRef]]()

  def receive: Receive = {
    case Join(streamId, client) =>
      clients(streamId) += client

    case Leave(streamId, client) =>
      clients(streamId) -= client

    case Broadcast(streamId, message) =>
      if (!clients.contains(streamId)) {
        clients(streamId) = Set.empty
      }
      clients(streamId).foreach(_ ! message)
  }
}

object LivestreamManagerActor {
  def props: Props = Props[LivestreamManagerActor]()

  case class Join(streamId: Long, client: ActorRef)
  case class Leave(streamId: Long, client: ActorRef)
  case class Broadcast(streamId: Long, message: ByteString)
}