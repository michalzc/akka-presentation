package michalz.akkapresentation.sac.services.sac

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.ServiceNotFound
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, FoundAvailability, RequestAvailabilities, RequestSpecificAvailabilities}
import michalz.akkapresentation.sac.services.FinderActor
import michalz.akkapresentation.sac.services.serviceregistry.ServiceRegistryComponent

import scala.collection.mutable.{HashMap => MutableHashMap, Map => MutableMap}
import scala.concurrent.Future
import scala.concurrent.duration._


/**
 * Created by michal on 15.03.15.
 */
trait DirectSacServiceComponent extends SacServiceComponent {
  this: ServiceRegistryComponent =>

  class DirectSacService extends SacService {
    import context.dispatcher
    implicit val askTimeout = Timeout(5.seconds)



    def receive = {
      case request: RequestAvailabilities => {
        //responses from finder actors as collection
        val listOfFutures = finderActors.values.map(ask(_, request).mapTo[FoundAvailability])
        combineAndSendSearchResults(listOfFutures, request.postCode)
      }

      case RequestSpecificAvailabilities(postCode, serviceList) => {
        val request = RequestAvailabilities(postCode)
        val listOfFutures = serviceList.map { serviceId =>
          finderActors.get(serviceId) match {
            case Some(finderActor) => ask(finderActor, request).mapTo[FoundAvailability]
            case None => Future{FoundAvailability(postCode, serviceId, ServiceNotFound)}
          }
        }

        combineAndSendSearchResults(listOfFutures, postCode)
      }
    }

    def combineAndSendSearchResults(listOfFutures: Iterable[Future[FoundAvailability]], postCode: String): Unit = {
      //all response futures combined into one future with sequence of responses
      val combinedFutures = Future.sequence(listOfFutures)

      //transformation of future content from sequence to one response class
      val future = combinedFutures.map { seqOfFoundAvailability =>
        val availabilitiesMap = seqOfFoundAvailability.map { foundAvailability =>
          (foundAvailability.serviceId -> foundAvailability.availability)
        }.toMap
        FoundAvailabilities(postCode, availabilitiesMap)
      }
      future pipeTo sender()
    }
  }

  def sacService = new DirectSacService
}
