package michalz.akkapresentation.sac.services.finders.texoffice

import michalz.akkapresentation.sac.domain.texoffice.TaxOfficeService
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

/**
 * Created by michal on 18.03.15.
 */
object TaxOfficeServiceSerializer {
  implicit object TaxOfficeServiceReader extends BSONDocumentReader[TaxOfficeService] {
    def read(bson: BSONDocument): TaxOfficeService = {
      val name = bson.getAs[String]("name").get
      val postCode = bson.getAs[String]("postCode").get
      val city = bson.getAs[String]("city").get
      val address = bson.getAs[String]("address").get
      val phone = bson.getAs[String]("phone").get
      val fax = bson.getAs[String]("fax")
      val email = bson.getAs[String]("email")
      val usId = bson.getAs[String]("usId")
      TaxOfficeService(name, postCode, city, address, phone, fax, email, usId)
    }
  }
}
