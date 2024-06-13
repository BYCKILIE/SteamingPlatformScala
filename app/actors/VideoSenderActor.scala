package actors

import akka.actor._

object VideoSenderActor {
  def props(out: ActorRef): Props = {
    Props(new VideoSenderActor(out))
  }
}

class VideoSenderActor(out: ActorRef) extends Actor {

  override def receive: Receive = {
    case image: String =>
      out ! image
  }

}
