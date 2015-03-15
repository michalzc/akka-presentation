package michalz.akkapresentation.sac.services

import akka.actor.{Props, Actor}

/**
 * Created by michal on 15.03.15.
 */
class SacService extends Actor {

  var availabilities: Map[String, List[String]] = Map()

  def receive = {
    case x => unhandled(x)
  }

}

object SacService {
  def props = Props[SacService]
}
