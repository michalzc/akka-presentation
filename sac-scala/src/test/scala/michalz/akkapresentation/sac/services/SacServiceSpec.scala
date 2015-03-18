package michalz.akkapresentation.sac.services

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.ServiceAvailability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, RequestAvailabilities}
import michalz.akkapresentation.sac.services.finders.Finder
import org.specs2.mutable.Specification
import akka.pattern.ask

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Success

/**
 * Created by michal on 16.03.15.
 */
class SacServiceSpec extends Specification {

  val testServiceName = "TestService"
  val testServiceId = "9999"

  implicit val system = ActorSystem("TestSystem")
  implicit val timeout = Timeout(Duration(5, "seconds"))

  val testSacServiceComponent = new TestSacServiceComponent(system, testServiceId, testServiceName)

  "This is a sac service specification" >> {
    "sac service asked for availability for any post code shall return no services" >> {
      val sacServiceRef = TestActorRef(testSacServiceComponent.sacService)
      val testPostCode = "11-111"

      val future = ask(sacServiceRef, RequestAvailabilities(testPostCode))
      val Success(response: FoundAvailabilities) = Await.ready(future, timeout.duration).value.get

      sacServiceRef must not be null
      response must not be null
      response.postCode must be equalTo testPostCode
      response.availabilities must haveSize(1)
      response.availabilities must haveKey(testServiceId)
      response.availabilities.get(testServiceId).get must haveClass[ServiceAvailability]
      response.availabilities.get(testServiceId).get.status must be equalTo "COMPLETED"
    }
  }
}

sealed class TestSacServiceComponent(val system: ActorSystem, val testServiceId: String, val testServiceName: String)
  extends SacServiceComponent with ServiceRegistryComponent {

  def serviceRegistry = new ServiceRegistry {
    override def services = Seq(
      new Finder {
        def actorSystem = system
        def serviceId = testServiceId
        def serviceName = testServiceName
        def serviceAvailability(postCode: String) =
          Future(new ServiceAvailability(postCode, serviceName))(system.dispatcher)
      }
    )
  }
}