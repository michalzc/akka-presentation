package michalz.akkapresentation.sac.services

import akka.actor.ActorSystem
import michalz.akkapresentation.sac.services.finders.shell.ShellGasStationFinder
import michalz.akkapresentation.sac.services.finders.texoffice.TaxOfficeFinder
import michalz.akkapresentation.sac.services.finders.{Finder, MongoHandler}

/**
 * Created by michal on 18.03.15.
 */

trait SacServiceRegistryComponent extends ServiceRegistryComponent {

  def system: ActorSystem
  def mongoHandler: MongoHandler

  class SacAppServiceRegistry extends ServiceRegistry {

    val services: Seq[Finder] = List(
      new ShellGasStationFinder("1001", "ShellGasStation", "gas_stations.shell.csv", system),
      new TaxOfficeFinder("2001", "TaxOffice", "akkapresentation", "taxoffices", mongoHandler, system)
    )
  }

  def serviceRegistry = new SacAppServiceRegistry
}
