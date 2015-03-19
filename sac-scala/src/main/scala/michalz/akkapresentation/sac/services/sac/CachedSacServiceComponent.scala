package michalz.akkapresentation.sac.services.sac

import java.util.UUID

import akka.actor.{ActorIdentity, ActorRef, Identify}
import michalz.akkapresentation.sac.domain.messages.RequestAvailabilities
import michalz.akkapresentation.sac.services.serviceregistry.ServiceRegistryComponent

/**
 * Created by michal on 19.03.15.
 */
class CachedSacServiceComponent extends SacServiceComponent {
  this: ServiceRegistryComponent =>

  class CachedSacService extends SacService {
    import context.become

    val sacCacheCorrelationId = UUID.randomUUID()

    var cacheActorRef: ActorRef = _
    var requests = scala.collection.mutable.HashMap[String, Seq[ActorRef]]()

    override def preStart = {
      super.preStart
      context.actorSelection("/user/sacCache") ! Identify(sacCacheCorrelationId)
    }

    def receive = {
      case ActorIdentity(correlationId, Some(ref)) => {
        if(sacCacheCorrelationId == correlationId) {
          cacheActorRef = ref
          become(mainReceive)
        }
      }
    }

    def mainReceive: Receive = {
      case RequestAvailabilities(postCode) => {
        val services = finderActors.keys
      }
    }
  }

  def sacService: SacService = new CachedSacService
}
