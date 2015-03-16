package michalz.akkapresentation.sac.services

import akka.actor.{ActorLogging, ActorRef, Props, Actor}
import michalz.akkapresentation.sac.domain.Availability
import michalz.akkapresentation.sac.services.SacServiceActor.RequestAvailabilities

/**
 * Created by michal on 15.03.15.
 */
class SacServiceActor extends Actor with ServiceRegistry with ActorLogging {

  var finderActors: Map[String, ActorRef] = Map()
  val requests: scala.collection.mutable.Map[String, List[ActorRef]] = scala.collection.mutable.HashMap()

  override def preStart = {
    log.info("Starting sacActor")
    finderActors = services.map{ finder =>
      (finder.serviceId -> context.actorOf(FinderActor.props(finder), finder.serviceName))
    }.toMap
  }

  def receive = {
    case request: RequestAvailabilities => {
      val recipients = requests.get(request.postCode).map( sender() :: _).getOrElse(List(sender()))
      requests.put(request.postCode, recipients)
      finderActors.values.foreach(_ ! request)
    }
  }

}

object SacServiceActor {
  def props = Props[SacServiceActor]()

  case class RequestAvailabilities(postCode: String)
  case class Availabilities(postCode: String, availabilities: Seq[Availability])
}
