package tbje.cluster

import akka.actor.{ Props, ActorSystem }
import scala.annotation.tailrec

object ChatClient extends App {

  val QuitCommand   = "q"

  val system = ActorSystem("ClusterSystem")

  val clientActor = system.actorOf(ClientActor.props())

  def enterName(): Unit = {
    print("Please provide a screen name: ")
    Console.readLine() match {
      case QuitCommand =>
        system.shutdown()
      case empty if empty.trim == "" =>
        println(s"Please try again.")
        enterName()
      case name =>
        println(s"Welcome $name, please chat away (q to quit)!")
        commandLoop(name)
    }
  }

  @tailrec
  def commandLoop(name: String): Unit = {
    Console.readLine() match {
      case QuitCommand =>
        system.shutdown()
      case msg =>
        clientActor ! (name -> msg)
        commandLoop(name)
    }

  }

  enterName()

}
