package michalz.akkapresentation.sac.services

import akka.actor.Props

/**
 * Created by michal on 16.03.15.
 */
object SacServiceWiring extends SacServiceComponent {
  def sacService = {
    new SacService with ServiceRegistry
  }
}
