package michalz.akkapresentation.sac.services.finders

import akka.actor.ActorSystem
import michalz.akkapresentation.sac.domain.ServiceAvailability

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by michal on 15.03.15.
 */
trait Finder {
  def serviceId: String
  def serviceName: String
  def serviceAvailability(postCode: String): Future[ServiceAvailability]
}
