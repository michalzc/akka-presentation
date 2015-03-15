package michalz.akkapresentation.sac.services

import akka.actor.{ActorRef, Props, Actor}
import michalz.akkapresentation.sac.domain.Availability

/**
 * Created by michal on 15.03.15.
 */
class SacService extends Actor with ServiceRegistry {

  var finderActors: Map[String, ActorRef] = _

  override def preStart = {
    finderActors = services.mapValues{ finder =>
      context.actorOf(FinderActor.props(finder), finder.serviceName)
    }
  }

  def receive = {
    case x => unhandled(x)
  }

}

object SacService {
  def props = Props[SacService]

  case class RequestAvailabilities(postCode: String)
  case class Availabilities(postCode: String, availabilities: Seq[Availability])
}
