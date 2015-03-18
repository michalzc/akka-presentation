package michalz.akkapresentation.sac.services.serviceregistry

import michalz.akkapresentation.sac.services.finders.Finder

/**
 * Created by michal on 15.03.15.
 */

trait ServiceRegistryComponent {

  trait ServiceRegistry {
    def services: Seq[Finder]
  }

  def serviceRegistry: ServiceRegistry

}


