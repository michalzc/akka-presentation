package michalz.akkapresentation.sac.services.finders

import michalz.akkapresentation.sac.domain.ServiceAvailability

/**
 * Created by michal on 15.03.15.
 */
trait Finder {
  def serviceId: String
  def serviceAvailability(postCode: String): ServiceAvailability
}
