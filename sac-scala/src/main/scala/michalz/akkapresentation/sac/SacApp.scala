package michalz.akkapresentation.sac

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import michalz.akkapresentation.sac.services.finders.MongoHandler
import michalz.akkapresentation.sac.services.{SacServiceComponent, ServiceRegistry}
import michalz.akkapresentation.sac.webapi.SacApiService
import reactivemongo.api.MongoDriver
import spray.can.Http

import scala.concurrent.duration._

/**
 * Created by michal on 15.03.15.
 */
object SacApp extends App {

  implicit val timeout = Timeout(5.seconds)
  implicit val system = ActorSystem("SacSystem")
  val mongoHandler = new MongoHandler(new MongoDriver(system), List("localhost"))

  val sacServiceComponent = new SacServiceComponent {
    def sacService: SacService = new SacService with ServiceRegistry {
      def actorSystem = SacApp.system
      def mongoHandler = SacApp.mongoHandler
    }
  }

  val sacActorRef = system.actorOf(Props(sacServiceComponent.sacService), "sacService")
  val apiActorRef = system.actorOf(SacApiService.props, "apiService")

  IO(Http) ? Http.Bind(apiActorRef, interface = "0.0.0.0", port = 8000)
}
