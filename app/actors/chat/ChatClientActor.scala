package actors.chat

import actors.chat.ChatManagerActor.{Broadcast, Join, Leave}
import akka.actor.{Actor, ActorRef, Props}

class ChatClientActor(id: Long, out: ActorRef, manager: ActorRef) extends Actor {

  override def preStart(): Unit = {
    manager ! Join(id, out)
  }

  override def postStop(): Unit = {
    manager ! Leave(id, out)
  }

  def receive: Receive = {
    case msg: String =>
      manager! Broadcast(id, msg)
  }

}

object ChatClientActor {
  def props(id: Long, out: ActorRef, manager: ActorRef): Props = Props(new ChatClientActor(id, out, manager))
}