package michalz.akkapresentation.sac.services

import akka.actor.ActorSystem
import michalz.akkapresentation.sac.services.finders.shell.ShellGasStationFinder
import michalz.akkapresentation.sac.services.finders.texoffice.TaxOfficeFinder
import michalz.akkapresentation.sac.services.finders.{Finder, MongoHandler}

/**
 * Created by michal on 18.03.15.
 */
trait SacAppServiceRegistry extends ServiceRegistry {
  def actorSystem: ActorSystem
  def mongoHandler: MongoHandler

  val services: Seq[Finder] = List(
    new ShellGasStationFinder("1001", "ShellGasStation",  "gas_stations.shell.csv", actorSystem),
    new TaxOfficeFinder("2001", "TaxOffice", "akkapresentation", "taxoffices", mongoHandler, actorSystem)

  )
}
