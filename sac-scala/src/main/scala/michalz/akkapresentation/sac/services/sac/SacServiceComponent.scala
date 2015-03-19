package michalz.akkapresentation.sac.services.sac

import akka.actor.{Actor, ActorLogging, ActorRef}
import michalz.akkapresentation.sac.services.FinderActor
import michalz.akkapresentation.sac.services.serviceregistry.ServiceRegistryComponent

/**
 * Created by michal on 18.03.15.
 */
trait SacServiceComponent {
  this: ServiceRegistryComponent =>

  trait SacService extends Actor with ActorLogging {
    var finderActors: Map[String, ActorRef] = Map()

    override def preStart = {
      log.info("Starting sacActor")
      finderActors = serviceRegistry.services.map { finder =>
        (finder.serviceId -> context.actorOf(FinderActor.props(finder), finder.serviceName))
      }.toMap
    }
  }

  def sacService: SacService
}
