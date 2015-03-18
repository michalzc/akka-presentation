package michalz.akkapresentation.sac.services.finders

import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONRegex, BSONDocument}

/**
 * Created by michal on 17.03.15.
 */
trait MongoFinder extends Finder {

  def mongoHandler: MongoHandler
  def dbName: String
  def collectionName: String

  def buildQuery(postCodeField: String, postCode: String): BSONDocument = {
    val postCodeStart: String = postCode.substring(0, 2)
    BSONDocument(postCodeField -> BSONRegex(s"^$postCodeStart-\\.*", ""))
  }

}

class MongoHandler(mongoDriver: MongoDriver, dbHosts: List[String]) {

  import mongoDriver.system.dispatcher

  val connection = mongoDriver.connection(dbHosts)
  def collection(dbName: String, collectionName: String) = {
    val db = connection(dbName)
    db[BSONCollection](collectionName)
  }

}
