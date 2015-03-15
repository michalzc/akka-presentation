package michalz.akkapresentation.sac.services

import akka.actor.{Props, Actor}
import michalz.akkapresentation.sac.services.finders.Finder

/**
 * Created by michal on 16.03.15.
 */
class FinderActor(private val finder: Finder) extends Actor {

  def receive = ???
}

object FinderActor {
  def props(finder: Finder) = Props(new FinderActor(finder))
}
