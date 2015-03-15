package michalz.akkapresentation.sac.services

import michalz.akkapresentation.sac.services.finders.shell.ShellGasStationFinder

/**
 * Created by michal on 15.03.15.
 */
trait ServiceRegistry {
  val services = Map("1001", new ShellGasStationFinder("1001", "gas_stations.shell.csv"))
}
