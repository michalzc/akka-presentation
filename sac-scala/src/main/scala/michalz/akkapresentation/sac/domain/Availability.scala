package michalz.akkapresentation.sac.domain

/**
 * Created by michal on 15.03.15.
 */
trait Availability {
  val status: String
}

object SearchInProgress extends Availability {
  val status = "SEARCH_IN_PROGRESS"
}

object ServiceNotFound extends Availability {
  val status = "SERVICE_NOT_FOUND"
}

trait Service {
  val postCode: String
}

class ServiceAvailability(val postCode: String,
                          val serviceId: String,
                          val exactMatches: List[Service],
                          val nearMatches: List[Service]) extends Availability {

  val status = "COMPLETED"

  def this(postCode: String, serviceId: String) = {
    this(postCode, serviceId, List(), List())
  }

  def addExactMatch(service: Service) = {
    new ServiceAvailability(postCode, serviceId, service :: exactMatches, nearMatches)
  }

  def addNearMatch(service: Service) = {
    new ServiceAvailability(postCode, serviceId, exactMatches, service :: nearMatches)
  }
}
