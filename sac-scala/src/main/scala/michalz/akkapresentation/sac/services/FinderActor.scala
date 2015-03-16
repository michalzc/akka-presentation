package michalz.akkapresentation.sac.services

import akka.actor.{ActorLogging, Props, Actor}
import michalz.akkapresentation.sac.services.finders.Finder

/**
 * Created by michal on 16.03.15.
 */
class FinderActor(private val finder: Finder) extends Actor with ActorLogging {

  override def preStart = {
    log.info("Actor for {} created", finder.serviceName)
  }

  def receive = {
    case x => unhandled(x)
  }
}

object FinderActor {
  def props(finder: Finder) = Props(new FinderActor(finder))
}
