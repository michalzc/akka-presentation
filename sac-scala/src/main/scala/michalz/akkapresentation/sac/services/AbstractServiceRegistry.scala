package michalz.akkapresentation.sac.services

import michalz.akkapresentation.sac.services.finders.Finder
import michalz.akkapresentation.sac.services.finders.shell.ShellGasStationFinder

/**
 * Created by michal on 15.03.15.
 */
trait AbstractServiceRegistry {
  def services: Seq[Finder]
}

trait ServiceRegistry extends AbstractServiceRegistry {
  val services: Seq[Finder] = List(new ShellGasStationFinder("1001", "ShellGasStation",  "gas_stations.shell.csv"))
}
