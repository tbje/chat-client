package tbje.cluster

import akka.actor.ActorRef

object ServerActor {
  case class Subscribe(ref: ActorRef)
  case class Message(sender: String, msg: String)
}
