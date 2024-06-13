package actors.chat

import actors.chat.ChatManagerActor.{Broadcast, Join, Leave}
import akka.actor._

import scala.collection.mutable

class ChatManagerActor extends Actor {
  private var clients = new mutable.HashMap[Long, Set[ActorRef]]()

  def receive: Receive = {
    case Join(chatId, client) =>
      if (!clients.contains(chatId)) {
        clients(chatId) = Set.empty
      }
      clients(chatId) += client

    case Leave(streamId, client) =>
      clients(streamId) -= client

    case Broadcast(streamId, message) =>
      clients(streamId).foreach(_ ! message)
  }
}

object ChatManagerActor {
  def props: Props = Props[ChatManagerActor]()

  case class Join(streamId: Long, client: ActorRef)
  case class Leave(streamId: Long, client: ActorRef)
  case class Broadcast(streamId: Long, message: String)
}