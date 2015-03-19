package michalz.akkapresentation.sac.services.cache

import akka.actor.{Actor, ActorLogging}
import michalz.akkapresentation.sac.domain.{Availability, ServiceAvailability}
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, FoundAvailability, RequestSpecificAvailabilities}

/**
 * Created by michal on 19.03.15.
 */
trait CacheActorComponent {
  this: CacheServiceComponent =>

  class CacheActor extends Actor with ActorLogging {

    private val cache = cacheService("sacCache")

    def receive: Receive = {
      case RequestSpecificAvailabilities(postCode, services) => {
        val availabilities: Map[String, Availability] = (for {
          serviceId <- services
          serviceAvailability <- cache.getFromCache[(String, String), ServiceAvailability]((postCode, serviceId))
        } yield (serviceId, serviceAvailability)).toMap
        sender ! FoundAvailabilities(postCode, availabilities)
      }

      case FoundAvailability(postCode, serviceId, availability) => {
        cache.putToCache((postCode, serviceId), availability)
      }
    }

    override def postStop = {
      cache.shutdownCache
    }
  }

  def cacheActor = new CacheActor
}
