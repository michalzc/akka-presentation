package michalz.akkapresentation.sac.domain.shell

import michalz.akkapresentation.sac.domain.ServiceAvailability

/**
 * Created by michal on 15.03.15.
 */
class ShellGasStationAvailability(
                                   val postCode: String,
                                   val exactMatch: List[ShellGasStationService],
                                   val nearMatch: List[ShellGasStationService]) extends ServiceAvailability {

  def addExactMatch(shellGasStationService: ShellGasStationService) = {
    new ShellGasStationAvailability(postCode, shellGasStationService :: exactMatch, nearMatch)
  }

  def addNearMatch(shellGasStationService: ShellGasStationService) = {
    new ShellGasStationAvailability(postCode, exactMatch, shellGasStationService :: nearMatch)
  }
}
