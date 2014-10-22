package tbje.cluster

import akka.actor._
import akka.cluster._
import akka.cluster.ClusterEvent._
import scala.collection.mutable.Buffer

object ClientActor {
  def props() = Props(classOf[ClientActor])
  case class Subscribe(ref: ActorRef)
  case class Message(sender: String, msg: String)
}

class ClientActor extends Actor with ActorLogging {
  import ClientActor._

  val cluster = Cluster(context.system)

  override def preStart() = {
    cluster.subscribe(self, classOf[MemberUp])
    cluster.subscribe(self, classOf[MemberRemoved])
    cluster.subscribe(self, classOf[UnreachableMember])
    cluster.subscribe(self, classOf[ReachableMember])
  }

  override def postStop() = cluster.unsubscribe(self)

  val servers: Buffer[Member] = Buffer.empty

  def addIfServer(m : Member) {
    if (m.hasRole("server")) {
      servers += m
    }
  }

  def messageServers(user: String, msg: String) {
    import tbje.cluster.ServerActor.Message
    import akka.actor._
    servers foreach { m =>
      context.actorSelection(RootActorPath(m.address) / "user" / "theserver") ! Message(user, msg)
    }
  }

  def receive: Receive = {
    case (user: String, msg: String) =>
      messageServers(user, msg)
    case state: CurrentClusterState =>
      state.members.filter(_.status == MemberStatus.Up) foreach addIfServer
    case MemberUp(m) =>
      addIfServer(m)
    case ReachableMember(m) =>
      addIfServer(m)
    case UnreachableMember(m) =>
      servers -= m
    case MemberRemoved(m, _) =>
      servers -= m
    case _ =>
  }
}
