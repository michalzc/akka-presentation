package michalz.akkapresentation.sac.webapi

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.Availability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, RequestAvailabilities}
import org.json4s.{FieldSerializer, NoTypeHints}
import org.json4s.jackson.Serialization
import spray.httpx.Json4sJacksonSupport
import spray.routing.HttpServiceActor

import scala.concurrent.duration._

/**
 * Created by michal on 15.03.15.
 */
class SacApiService extends HttpServiceActor with ActorLogging with Json4sJacksonSupport {
  import context.become

  implicit def json4sJacksonFormats = Serialization.formats(NoTypeHints) + FieldSerializer[Availability]() 
  implicit def timeout = Timeout(30.seconds)
  implicit val ec = context.dispatcher //try to remove!

  var sacServiceRef: ActorRef = _

  val sacCorrelationId = "sacServiceId"

  override def preStart = {
    log.info("Starting api actor")
    context.actorSelection("/user/sacService") ! Identify(sacCorrelationId)
  }

  def receive = {
    case ActorIdentity(correlationId, Some(actorRef)) => {
      if(sacCorrelationId == correlationId) {
        sacServiceRef = actorRef
        become(route)
        log.info("Received identity from sacService, routing enabled")
      } else {
        log.warning("Received identity from unknown actor correlationId: {}, actorRef: {}", correlationId, actorRef)
      }
    }
  }

  def route: Receive = runRoute {
    pathPrefix("availability") {
      get {
        path(Segment) { postCode =>
          complete {
            ask(sacServiceRef, RequestAvailabilities(postCode)).mapTo[FoundAvailabilities]
          }
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
  }
}

object SacApiService {
  def props = Props[SacApiService]
}
