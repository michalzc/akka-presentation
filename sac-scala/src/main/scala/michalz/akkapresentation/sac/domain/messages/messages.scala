package michalz.akkapresentation.sac.domain.messages

import java.util.UUID

import michalz.akkapresentation.sac.domain.Availability

case class RequestAvailabilities(postCode: String)
case class RequestSpecificAvailabilities(postCode: String, services: Seq[String], requestId: Option[UUID])
case class FoundAvailability(postCode: String, serviceId: String, availability: Availability)
case class FoundAvailabilities(postCode: String, availabilities: Map[String, Availability], requestId: Option[UUID])
