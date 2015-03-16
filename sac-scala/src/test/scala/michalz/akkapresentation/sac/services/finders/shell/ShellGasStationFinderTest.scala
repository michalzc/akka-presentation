package michalz.akkapresentation.sac.services.finders.shell

import michalz.akkapresentation.sac.domain.{ServiceAvailability, Service}
import org.specs2.mutable.Specification

/**
 * Created by michal on 15.03.15.
 */
class ShellGasStationFinderTest extends Specification {

  val noInstancePostCode = "51-354"
  val oneInstancePostCode = "04-175"
  val twoInstancesPostCode = "05-270"

  def finder = new ShellGasStationFinder("1001", "testServiceId", "gas_stations.shell.test.csv")

  "this is shell finder specification " >> {
    "where finder must return availability object with same post code without services" >> {
      val availability = finder.serviceAvailability(noInstancePostCode)
      availability.postCode must beEqualTo(noInstancePostCode)
      availability.exactMatch must beEmpty
      availability.nearMatch must beEmpty
    }

    "where finder must return availability with one match" >> {
      val availability = finder.serviceAvailability(oneInstancePostCode)
      availability.exactMatch must have size (1)
      availability.exactMatch(0).postCode must beEqualTo(oneInstancePostCode)
      availability.nearMatch must beEmpty
    }

    "where finder must return availability with two matches" >> {
      val availability = finder.serviceAvailability(twoInstancesPostCode)
      availability.exactMatch must have size (1)
      availability.exactMatch(0).postCode must be equalTo(twoInstancesPostCode)
      availability.nearMatch must have size (1)
      availability.nearMatch(0) must not be equalTo(twoInstancesPostCode)
    }
  }

}
