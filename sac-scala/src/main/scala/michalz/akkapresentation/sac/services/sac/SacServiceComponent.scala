package michalz.akkapresentation.sac.services.sac

import akka.actor.{ActorLogging, Actor}
import michalz.akkapresentation.sac.services.serviceregistry.{ServiceRegistryComponent, SacServiceRegistryComponent}

/**
 * Created by michal on 18.03.15.
 */
trait SacServiceComponent {
  trait SacService extends Actor with ActorLogging
  def sacService: SacService
}
