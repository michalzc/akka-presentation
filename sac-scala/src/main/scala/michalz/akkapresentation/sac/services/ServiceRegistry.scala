package michalz.akkapresentation.sac.services

import michalz.akkapresentation.sac.services.finders.Finder
import michalz.akkapresentation.sac.services.finders.shell.ShellGasStationFinder

/**
 * Created by michal on 15.03.15.
 */
trait ServiceRegistry {
  val services: Map[String, Finder] = Map("1001", new ShellGasStationFinder("1001", "ShellGasStation",  "gas_stations.shell.csv"))
}
