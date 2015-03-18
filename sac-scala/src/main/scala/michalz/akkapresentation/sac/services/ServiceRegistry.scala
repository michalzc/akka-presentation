package michalz.akkapresentation.sac.services

import akka.actor.ActorSystem
import michalz.akkapresentation.sac.services.finders.texoffice.TaxOfficeFinder
import michalz.akkapresentation.sac.services.finders.{MongoHandler, Finder}
import michalz.akkapresentation.sac.services.finders.shell.ShellGasStationFinder

/**
 * Created by michal on 15.03.15.
 */

trait ServiceRegistryComponent {

  trait ServiceRegistry {
    def services: Seq[Finder]
  }

  def serviceRegistry: ServiceRegistry

}


