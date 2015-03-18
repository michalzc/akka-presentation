package michalz.akkapresentation.sac.services.finders.shell

import akka.actor.ActorSystem
import michalz.akkapresentation.sac.domain.ServiceAvailability
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.Success
/**
 * Created by michal on 15.03.15.
 */
class ShellGasStationFinderSpec extends Specification with AfterAll {

  val system = ActorSystem("SGSFSSystem")
  val noInstancePostCode = "51-354"
  val oneInstancePostCode = "04-175"
  val twoInstancesPostCode = "05-270"
  val duration = Duration(5, "seconds")

  def afterAll() = {
    system.shutdown()
    system.awaitTermination()
  }

  def finder = new ShellGasStationFinder("1001", "testServiceId", "gas_stations.shell.test.csv", system)

  "this is shell finder specification " >> {
    "where finder must return availability object with same post code without services" >> {
      val future = finder.serviceAvailability(noInstancePostCode)
      val Success(availability: ServiceAvailability) = Await.ready(future, duration).value.get
      availability.postCode must beEqualTo(noInstancePostCode)
      availability.exactMatch must beEmpty
      availability.nearMatch must beEmpty
    }

    "where finder must return availability with one match" >> {
      val future = finder.serviceAvailability(oneInstancePostCode)
      val Success(availability: ServiceAvailability) = Await.ready(future, duration).value.get
      availability.exactMatch must have size (1)
      availability.exactMatch(0).postCode must beEqualTo(oneInstancePostCode)
      availability.nearMatch must beEmpty
    }

    "where finder must return availability with two matches" >> {
      val future = finder.serviceAvailability(twoInstancesPostCode)
      val Success(availability: ServiceAvailability) = Await.ready(future, duration).value.get
      availability.exactMatch must have size (1)
      availability.exactMatch(0).postCode must be equalTo(twoInstancesPostCode)
      availability.nearMatch must have size (1)
      availability.nearMatch(0) must not be equalTo(twoInstancesPostCode)
    }
  }

}
