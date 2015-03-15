package michalz.akkapresentation.sac.domain.shell

import michalz.akkapresentation.sac.domain.Service

/**
 * Created by michal on 15.03.15.
 */
case class ShellGasStationService(
                                   siteId: String,
                                   postCode: String,
                                   address: String,
                                   city: String,
                                   mainPhone: String,
                                   additionalPhone: String,
                                   email: String) extends Service
