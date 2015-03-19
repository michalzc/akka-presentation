package michalz.akkapresentation.sac.services.cache

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import michalz.akkapresentation.sac.domain.messages.{FoundAvailabilities, FoundAvailability,
RequestSpecificAvailabilities}
import michalz.akkapresentation.sac.domain.{Service, ServiceAvailability}
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success

/**
 * Created by michal on 19.03.15.
 */
class CacheActorSpec extends Specification with AfterAll {

  implicit val system = ActorSystem("CacheActorSpecSystem")
  implicit val timeout = Timeout(5.seconds)

  val cacheActorComponent = new CacheActorComponent with EHCacheServiceComponent {
    val cacheName = "cacheActorSpecCache"
  }

  def afterAll = {
    system.shutdown
    system.awaitTermination
  }

  val testPostCode1 = "51-111"
  val testServiceId = "9999"
  val testAvailability = new ServiceAvailability(testPostCode1, testServiceId, List(new Service {
    val postCode: String = testPostCode1
  }), List())
  
  val cacheActorRef = TestActorRef(cacheActorComponent.cacheActor)

  "this is specification for cache actor" >> {
    "cache actor shall return cached availability" >> {

      cacheActorRef ! FoundAvailability(testPostCode1, testServiceId, testAvailability)

      val Success(response) = Await.ready(ask(cacheActorRef, RequestSpecificAvailabilities(testPostCode1,
        Seq(testServiceId))), timeout.duration).mapTo[FoundAvailabilities].value.get

      response.postCode must be equalTo (testPostCode1)
      response.availabilities.keys must containAllOf(Seq(testServiceId))
    }
  }
}
