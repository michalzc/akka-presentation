package michalz.akkapresentation.sac.services

import akka.actor.{Props, ActorSystem}
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.ServiceAvailability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, RequestAvailabilities}
import michalz.akkapresentation.sac.services.finders.Finder
import org.specs2.mutable.Specification

import scala.concurrent.{Future, ExecutionContext, Await}
import scala.concurrent.duration._
import scala.util.Success

/**
 * Created by michal on 16.03.15.
 */
class SacServiceTest extends Specification {

  val testServiceName = "TestService"
  val testServiceId = "9999"

  implicit val system = ActorSystem("TestSystem")
  implicit val timeout = Timeout(Duration(5, "seconds"))

  val testSacServiceComponent = new TestSacServiceComponent(testServiceId, testServiceName, system)

  "This is a sac service specification" >> {
    "sac service asked for availability for any post code shall return no services" >> {
      val sacServiceRef = TestActorRef(testSacServiceComponent.sacService)
      val testPostCode = "11-111"

      val future = sacServiceRef ? RequestAvailabilities(testPostCode)
      val Success(response: FoundAvailabilities) = Await.ready(future, timeout.duration).value.get

      sacServiceRef must not be null
      response must not be null
      response.postCode must be equalTo testPostCode
      response.availabilities must haveSize(1)
      response.availabilities must haveKey(testServiceId)
      response.availabilities.get(testServiceId).get must haveClass[ServiceAvailability]
    }
  }
}

class TestSacServiceComponent(val testServiceId: String, val testServiceName: String, val system: ActorSystem) extends SacServiceComponent{

    def sacService = {
      new SacService {
        def services = Seq(
          new Finder {
            def actorSystem = system
            def serviceId = testServiceId
            def serviceName = testServiceName
            def serviceAvailability(postCode: String) = Future(new ServiceAvailability(postCode, serviceName))(system.dispatcher)
          }
        )
      }
    }
}
