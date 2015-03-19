package michalz.akkapresentation.sac.services.sac

import java.util.UUID

import akka.actor.{ActorIdentity, ActorRef, Identify}
import michalz.akkapresentation.sac.domain.{ServiceNotFound, SearchInProgress}
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, RequestSpecificAvailabilities,
RequestAvailabilities}
import michalz.akkapresentation.sac.services.serviceregistry.ServiceRegistryComponent

/**
 * Created by michal on 19.03.15.
 */
trait CachedSacServiceComponent extends SacServiceComponent {
  this: ServiceRegistryComponent =>

  class CachedSacService extends SacService {

    import context.become

    val sacCacheCorrelationId = uniqueId

    var cacheActorRef: ActorRef = _
    var requests = scala.collection.mutable.HashMap[UUID, Request]()

    override def preStart = {
      super.preStart
      context.actorSelection("/user/sacCache") ! Identify(sacCacheCorrelationId)
    }

    def receive = {
      case ActorIdentity(correlationId, Some(ref)) => {
        if (sacCacheCorrelationId == correlationId) {
          cacheActorRef = ref
          become(mainReceive)
        }
      }
    }

    def mainReceive: Receive = {
      case RequestAvailabilities(postCode) => {
        val services = finderActors.keys.toSeq
        val requestId = uniqueId
        cacheActorRef ! RequestSpecificAvailabilities(postCode, services, Some(requestId))
        requests.put(requestId, Request(sender(), finderActors.keys.toSeq))
      }

      case RequestSpecificAvailabilities(postCode, services, None) => {
        val requestId = uniqueId
        cacheActorRef ! RequestSpecificAvailabilities(postCode, services, Some(requestId))
        requests.put(requestId, Request(sender(), services))
      }

      case FoundAvailabilities(postCode, availabilities, Some(requestId)) => {
        requests.get(requestId) match {
          case None => log.warning("No waiting request for cache response")
          case Some(request) => {
            //list of services not found in cache
            val nonCachedServices = request.requestedServices.filterNot(availabilities.keys.toSeq.contains)
            
            //extract services to find(finder exists) and unknown ones
            val (servicesToFind, missingServices) = nonCachedServices partition finderActors.keys.toSeq.contains
            
            //build availabilites map
            val availabiltiesToResponse = availabilities ++ servicesToFind.map((_, SearchInProgress)) ++ 
              missingServices.map((_, ServiceNotFound))
            
            //send collected availabilities to requestor 
            request.requestor ! FoundAvailabilities(postCode, availabiltiesToResponse, None)
            
            //send request to finders
            for {
              serviceId <- servicesToFind
              finder <- finderActors get serviceId
            } {
              finder.tell(RequestAvailabilities(postCode), cacheActorRef)
            }
            
            requests.remove(requestId)
          }
        }
      }
    }
  }

  def uniqueId: UUID = UUID.randomUUID

  def sacService: SacService = new CachedSacService

  case class Request(requestor: ActorRef, requestedServices: Seq[String])

}
