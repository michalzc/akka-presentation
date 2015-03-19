package michalz.akkapresentation.sac.services.sac

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.ServiceAvailability
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, RequestAvailabilities, RequestSpecificAvailabilities}
import michalz.akkapresentation.sac.services.finders.Finder
import michalz.akkapresentation.sac.services.serviceregistry.ServiceRegistryComponent
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Success

/**
 * Created by michal on 16.03.15.
 */
class DirectSacServiceSpec extends Specification with AfterAll with TestDirectSacServiceComponent {

  val testServiceName = "TestService"
  val testServiceId = "9999"
  val testPostCode = "11-111"
  val nonExistingServices: List[String] = List("8888", "7777")

  implicit val system = ActorSystem("TestSystem")
  implicit val timeout = Timeout(Duration(5, "seconds"))

  val sacServiceRef = TestActorRef(sacService)


  override def afterAll = {
    system.shutdown()
    system.awaitTermination(timeout.duration)
  }

  "This is a sac service specification" >> {
    "sac service asked for availability for any post code shall return no services" >> {

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

    "sac service asked for availability for specific and non exists shall return not found response" >> {

      val future = ask(sacServiceRef, RequestSpecificAvailabilities(testPostCode, nonExistingServices, None))
      val Success(response: FoundAvailabilities) = Await.ready(future, timeout.duration).value.get

      response.postCode must be equalTo testPostCode
      response.availabilities must have size(2)
      response.availabilities must have keys(nonExistingServices: _*)
      response.availabilities.values.map(_.status) must contain(beEqualTo("SERVICE_NOT_FOUND")).foreach
    }
  }

}

trait TestDirectSacServiceComponent extends DirectSacServiceComponent with ServiceRegistryComponent {
  
  def system: ActorSystem
  def testServiceId: String
  def testServiceName: String
  
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
