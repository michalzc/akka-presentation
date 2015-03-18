package michalz.akkapresentation.sac.services

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.Availability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, FoundAvailability, RequestAvailabilities}

import scala.collection.mutable.{HashMap => MutableHashMap, Map => MutableMap}
import scala.concurrent.Future
import scala.concurrent.duration._


/**
 * Created by michal on 15.03.15.
 */
trait SacServiceComponent {
  this: ServiceRegistryComponent =>

  class SacService extends Actor with ActorLogging {
    import context.dispatcher
    implicit val askTimeout = Timeout(5.seconds)

    var finderActors: Map[String, ActorRef] = Map()

    override def preStart = {
      log.info("Starting sacActor")
      finderActors = serviceRegistry.services.map { finder =>
        (finder.serviceId -> context.actorOf(FinderActor.props(finder), finder.serviceName))
      }.toMap
    }

    def receive = {
      case request: RequestAvailabilities => {
        //responses from finder actors as collection
        val listOfFutures: Iterable[Future[FoundAvailability]] = finderActors.values.map(ask(_, request).mapTo[FoundAvailability])
        
        //all response futures combined into one future with sequence of responses
        val combinedFutures: Future[Iterable[FoundAvailability]] = Future.sequence(listOfFutures)
        
        //transformation of future content from sequence to one response class
        val future: Future[FoundAvailabilities] = combinedFutures.map { seqOfFoundAvailability =>
          val availabilitiesMap: Map[String, Availability] = seqOfFoundAvailability.map { foundAvailability =>
            (foundAvailability.serviceId -> foundAvailability.availability)
          }.toMap
          FoundAvailabilities(request.postCode, availabilitiesMap)
        }
        future pipeTo sender()
      }
    }
  }

  def sacService = new SacService
}
