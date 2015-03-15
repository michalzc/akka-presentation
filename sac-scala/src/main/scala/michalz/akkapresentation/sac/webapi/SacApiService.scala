package michalz.akkapresentation.sac.webapi

import akka.actor.ActorLogging
import spray.routing.HttpServiceActor

/**
 * Created by michal on 15.03.15.
 */
class SacApiService extends HttpServiceActor with ActorLogging {


  override def preStart = {
    log.info("Starting api actor")
  }

  def receive = runRoute({
    pathPrefix("availability") {
      get {
        path(Segment) { postCode =>
          complete(s"You requested for all service available in $postCode")
        } ~
        path(Segment / Segment) { (postCode, serviceList) =>
          val services = serviceList.split("[,\\.]").map(_.trim)
          val servicesAsString = services.mkString(", ")
          complete(s"You requested for services $servicesAsString in $postCode")
        } ~
        pathEndOrSingleSlash {
          complete("Ok")
        }
      }
    }
  })
}
