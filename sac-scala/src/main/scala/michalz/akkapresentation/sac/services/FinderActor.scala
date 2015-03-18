package michalz.akkapresentation.sac.services

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import michalz.akkapresentation.sac.domain.ServiceAvailability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailability, RequestAvailabilities}
import michalz.akkapresentation.sac.services.finders.Finder

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by michal on 16.03.15.
 */
class FinderActor(private val finder: Finder) extends Actor with ActorLogging {

  implicit val ec = context.system.dispatcher

  override def preStart = {
    log.info("Actor for {} created", finder.serviceName)
  }

  def receive = {
    case RequestAvailabilities(postCode) => {
      log.info("Received availability request for {}", postCode)
      val futureAvailability: Future[ServiceAvailability] = finder.serviceAvailability(postCode)
      futureAvailability.map{ availability =>
        FoundAvailability(postCode, finder.serviceId, availability)
      } pipeTo sender()
    }
  }
}

object FinderActor {
  def props(finder: Finder) = Props(new FinderActor(finder))
}
