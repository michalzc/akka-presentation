package michalz.akkapresentation.sac.services

import akka.actor.ActorSystem
import michalz.akkapresentation.sac.services.finders.texoffice.TaxOfficeFinder
import michalz.akkapresentation.sac.services.finders.{MongoHandler, Finder}
import michalz.akkapresentation.sac.services.finders.shell.ShellGasStationFinder

/**
 * Created by michal on 15.03.15.
 */
trait AbstractServiceRegistry {
  def services: Seq[Finder]
}

trait ServiceRegistry extends AbstractServiceRegistry {
  def actorSystem: ActorSystem
  def mongoHandler: MongoHandler

  val services: Seq[Finder] = List(
    new ShellGasStationFinder("1001", "ShellGasStation",  "gas_stations.shell.csv", actorSystem),
    new TaxOfficeFinder("2001", "TaxOffice", "akkapresentation", "taxoffices", mongoHandler, actorSystem)

  )
}
