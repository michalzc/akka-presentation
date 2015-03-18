package michalz.akkapresentation.sac.services.finders.texoffice

import akka.actor.ActorSystem
import michalz.akkapresentation.sac.domain.ServiceAvailability
import michalz.akkapresentation.sac.domain.texoffice.TaxOfficeService
import michalz.akkapresentation.sac.services.finders.{MongoFinder, MongoHandler}
import reactivemongo.api.collections.default.BSONCollection

import scala.concurrent.Future

/**
 * Created by michal on 17.03.15.
 */
class TaxOfficeFinder(val serviceId: String,
                      val serviceName: String,
                      val dbName: String,
                      val collectionName: String,
                      val mongoHandler: MongoHandler,
                      val system: ActorSystem) extends MongoFinder {

  def serviceAvailability(postCode: String): Future[ServiceAvailability] = {
    import TaxOfficeServiceSerializer.TaxOfficeServiceReader
    import system.dispatcher

    val collection: BSONCollection = mongoHandler.collection(dbName, collectionName)
    val eventualServices: Future[Seq[TaxOfficeService]] = collection.find(buildQuery("postCode", postCode)).cursor[TaxOfficeService]
      .collect[Seq]()

    eventualServices.map { collection =>
      collection.foldLeft(new ServiceAvailability(postCode, serviceId)) { (availability, service) =>
        if (postCode == service.postCode) availability.addExactMatch(service)
        else availability.addNearMatch(service)
      }
    }
  }
}
