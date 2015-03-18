package michalz.akkapresentation.sac.domain.messages

import michalz.akkapresentation.sac.domain.Availability

case class RequestAvailabilities(postCode: String)
case class RequestSpecificAvailabilities(postCode: String, services: Seq[String])
case class FoundAvailability(postCode: String, serviceId: String, availability: Availability)
case class FoundAvailabilities(postCode: String, availabilities: Map[String, Availability])
