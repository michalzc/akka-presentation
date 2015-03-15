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

trait ServiceAvailability extends Availability {
  def postCode: String
  def exactMatch: List[_ <: Service]
  def nearMatch: List[_ <: Service]
}
