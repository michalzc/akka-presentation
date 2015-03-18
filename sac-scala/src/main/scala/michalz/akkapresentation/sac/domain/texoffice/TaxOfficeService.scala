package michalz.akkapresentation.sac.domain.texoffice

import michalz.akkapresentation.sac.domain.Service

/**
 * Created by michal on 17.03.15.
 */
case class TaxOfficeService(name: String,
                            postCode: String,
                            city: String,
                            address: String,
                            phone: String,
                            fax: Option[String],
                            email: Option[String],
                            usId: Option[String]) extends Service
