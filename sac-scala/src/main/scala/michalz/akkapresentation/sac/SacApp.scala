package michalz.akkapresentation.sac

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import michalz.akkapresentation.sac.services.SacServiceWiring
import michalz.akkapresentation.sac.webapi.SacApiService
import spray.can.Http

import scala.concurrent.duration._

/**
 * Created by michal on 15.03.15.
 */
object SacApp extends App {

  implicit val timeout = Timeout(5.seconds)
  implicit val system = ActorSystem("SacSystem")
  val sacActorRef = system.actorOf(Props(SacServiceWiring.sacService), "sacService")
  val apiActorRef = system.actorOf(SacApiService.props, "apiService")

  IO(Http) ? Http.Bind(apiActorRef, interface = "0.0.0.0", port = 8000)
}
