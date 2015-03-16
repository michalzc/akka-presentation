package michalz.akkapresentation.sac.services

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.ServiceAvailability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, RequestAvailabilities}
import michalz.akkapresentation.sac.services.finders.Finder
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success

/**
 * Created by michal on 16.03.15.
 */
class SacServiceTest extends Specification {

  val testServiceName = "TestService"
  val testServiceId = "9999"

  def sacServiceComponent = new SacServiceComponent {
    def sacService = {
      new SacService {
        def services = Seq(
          new Finder {
            def serviceId = testServiceId
            def serviceName = testServiceName
            def serviceAvailability(postCode: String) = new ServiceAvailability(postCode, serviceName)
          }
        )
      }
    }
  }

  implicit val system = ActorSystem("TestSystem")
  implicit val timeout = Timeout(Duration(5, "seconds"))

  "This is a sac service specification" >> {
    "sac service asked for availability for any post code shall return no services" >> {
      val sacServiceRef = TestActorRef(sacServiceComponent.sacService)
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


