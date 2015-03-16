package michalz.akkapresentation.sac.services

import akka.actor.{Actor, ActorLogging, ActorRef}
import michalz.akkapresentation.sac.domain.Availability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, FoundAvailability, RequestAvailabilities}

import scala.collection.mutable.{HashMap => MutableHashMap, Map => MutableMap}


/**
 * Created by michal on 15.03.15.
 */
trait SacServiceComponent {

  trait SacService extends Actor with AbstractServiceRegistry with ActorLogging {

    var finderActors: Map[String, ActorRef] = Map()

    val ongoingRequests: MutableMap[String, OngoingRequest] = MutableHashMap()


    override def preStart = {
      log.info("Starting sacActor")
      finderActors = services.map { finder =>
        (finder.serviceId -> context.actorOf(FinderActor.props(finder), finder.serviceName))
      }.toMap
    }

    def receive = {

      case request: RequestAvailabilities => {
        log.info("Received request for availabilities for post code {} from {}", request.postCode, sender())
        val ongoingRequest = ongoingRequests.get(request.postCode).map(_.addRequestor(sender())).getOrElse(
          new OngoingRequest(request.postCode, sender()))
        ongoingRequests.put(request.postCode, ongoingRequest)
        finderActors.values.foreach(_ ! request)
      }

      case FoundAvailability(postCode, serviceId, availability) => {
        log.info("Received response from finder for post code {}", postCode)
        ongoingRequests.get(postCode) match {
          case Some(ongoingRequest) => {
            val newRequest = ongoingRequest.addAvailability(serviceId, availability)
            if (newRequest.isComplete) {
              log.info("Request is completed, sending response to requestors")
              ongoingRequests.remove(postCode)
              val foundAvailabilities: FoundAvailabilities = FoundAvailabilities(postCode, newRequest
                .collectedAvailabilities)

              newRequest.requestors.foreach{ req =>
                log.info("Sending response to {}", req)
                req ! foundAvailabilities
              }
            } else {
              log.info("Request is still not completed, waiting for other finders")
              ongoingRequests.put(postCode, newRequest)
            }
          }
          case None => {
            log.warning("Something went wrong, no ongoing request for {} post code", postCode)
          }
        }
      }

    }

    class OngoingRequest(val postCode: String, val requestors: List[ActorRef], val collectedAvailabilities: Map[String, Availability]) {
      def this(postCode: String, requestor: ActorRef) = {
        this(postCode, List(requestor), Map())
      }

      def addRequestor(requestor: ActorRef) = {
        new OngoingRequest(postCode, requestor :: requestors, collectedAvailabilities)
      }

      def addAvailability(serviceId: String, availability: Availability) = {
        new OngoingRequest(postCode, requestors, collectedAvailabilities + (serviceId -> availability))
      }

      def isComplete = collectedAvailabilities.size == SacService.this.finderActors.size
    }

  }


  def sacService: SacService

}
