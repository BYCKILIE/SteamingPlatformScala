package actors

import akka.actor._

object WebSocketActor {
  def props(out: ActorRef, id: String): Props = {
    Props(new WebSocketActor(out, id))
  }
}

class WebSocketActor(out: ActorRef, id: String) extends Actor {

  override def receive: Receive = {
    case msg: String =>
      println(id)
      out ! id
  }

}
