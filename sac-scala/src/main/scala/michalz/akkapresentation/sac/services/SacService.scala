package michalz.akkapresentation.sac.services

import akka.actor.{Props, Actor}
import michalz.akkapresentation.sac.domain.Availability

/**
 * Created by michal on 15.03.15.
 */
class SacService extends Actor with ServiceRegistry {

  def receive = {
    case x => unhandled(x)
  }

}

object SacService {
  def props = Props[SacService]

  case class RequestAvailabilities(postCode: String)
  case class Availabilities(postCode: String, availabilities: Seq[Availability])
}
