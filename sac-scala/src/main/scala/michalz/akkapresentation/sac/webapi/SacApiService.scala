package michalz.akkapresentation.sac.webapi

import java.util.UUID

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.Availability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, RequestAvailabilities,
RequestSpecificAvailabilities}
import org.json4s.jackson.Serialization
import org.json4s.{FieldSerializer, NoTypeHints}
import spray.http.StatusCodes.{BadRequest, InternalServerError}
import spray.httpx.Json4sJacksonSupport
import spray.routing.HttpServiceActor

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
 * Created by michal on 15.03.15.
 */
class SacApiService extends HttpServiceActor with ActorLogging with Json4sJacksonSupport {

  import context.become

  implicit def json4sJacksonFormats = Serialization.formats(NoTypeHints) + FieldSerializer[Availability]()

  implicit def timeout = Timeout(5.seconds)

  implicit val ec = context.dispatcher //try to remove!

  var sacServiceRef: ActorRef = _
  val sacCorrelationId = UUID.randomUUID()

  override def preStart = {
    log.info("Starting api actor")
    context.actorSelection("/user/sacService") ! Identify(sacCorrelationId)
  }

  def receive = {
    case ActorIdentity(correlationId, Some(actorRef)) => {
      if (sacCorrelationId == correlationId) {
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
          onComplete(ask(sacServiceRef, RequestAvailabilities(postCode)).mapTo[FoundAvailabilities]) {
            case Success(foundAvailabilities) => complete(foundAvailabilities)
            case Failure(ex) => complete(InternalServerError, s"An error has occured: ${ex.getMessage}")
          }

        } ~
          path(Segment / Segment) { (postCode, serviceList) =>
            val services = serviceList.split("[,\\.]").map(_.trim).filter(!_.isEmpty)
            log.info("Service list: {}", services.mkString(", "))
            if (services.isEmpty) {
              complete(BadRequest, "Invalid service list")
            } else {
              onComplete(ask(sacServiceRef, RequestSpecificAvailabilities(postCode, services, None))
                .mapTo[FoundAvailabilities]) {
                case Success(foundAvailabilities) => complete(foundAvailabilities)
                case Failure(ex) => complete(InternalServerError, s"An error has occured: ${ex.getMessage}")
              }
            }
          }
      }
    }
  }
}

object SacApiService {
  def props = Props[SacApiService]
}
