package michalz.akkapresentation.sac.domain

/**
 * Created by michal on 15.03.15.
 */
trait Availability {

}

trait Service {
  def postCode: String
}

object SearchInProgress extends Availability {

}

class ServiceAvailability(val postCode: String,
                          val serviceId: String,
                          val exactMatch: List[Service],
                          val nearMatch: List[Service]) extends Availability {

  def this(postCode: String, serviceId: String) = {
    this(postCode, serviceId, List(), List())
  }

  def addExactMatch(service: Service) = {
    new ServiceAvailability(postCode, serviceId, service :: exactMatch, nearMatch)
  }

  def addNearMatch(service: Service) = {
    new ServiceAvailability(postCode, serviceId, exactMatch, service :: nearMatch)
  }
}
