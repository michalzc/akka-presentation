package michalz.akkapresentation.sac.services.finders.shell

import michalz.akkapresentation.sac.domain.ServiceAvailability
import michalz.akkapresentation.sac.domain.shell.ShellGasStationAvailability
import michalz.akkapresentation.sac.services.finders.Finder

/**
 * Created by michal on 15.03.15.
 */
class ShellGasStationFinder(val serviceId: String, cvsFilePath: String) extends Finder{

  override def serviceAvailability(postCode: String): ServiceAvailability = new ShellGasStationAvailability(postCode, List(), List())
}
