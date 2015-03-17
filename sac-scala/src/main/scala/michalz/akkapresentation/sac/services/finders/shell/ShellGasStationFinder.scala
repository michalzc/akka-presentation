package michalz.akkapresentation.sac.services.finders.shell

import java.io.InputStreamReader

import michalz.akkapresentation.sac.domain.ServiceAvailability
import michalz.akkapresentation.sac.domain.shell.ShellGasStationService
import michalz.akkapresentation.sac.services.finders.Finder
import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord}

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

/**
 * This finder is suboptimal intentionally
 * Created by michal on 15.03.15.
 */

object ShellGasStationFinder {
  def mkService(record: CSVRecord): ShellGasStationService = {
    ShellGasStationService(
      record.get(0), record.get(1), record.get(2), record.get(4), record.get(3), record.get(7),
      if(record.get(8).trim.isEmpty) None else Some(record.get(8)), record.get(6))
  }
}

class ShellGasStationFinder(val serviceId: String, val serviceName: String, private val cvsFileName: String) extends Finder {

  def serviceAvailability(postCode: String)(implicit ec: ExecutionContext): Future[ServiceAvailability] = {
    val reader = new InputStreamReader(this.getClass.getClassLoader.getResourceAsStream(cvsFileName))
    try {
      Future {
        val records: CSVParser = CSVFormat.RFC4180.withDelimiter(';').parse(reader)
        records.foldLeft(new ServiceAvailability(postCode, serviceId)) { (availability, record) =>
          if (postCode == record.get(2)) {
            availability.addExactMatch(ShellGasStationFinder.mkService(record))
          } else if (record.get(2).startsWith(postCode.substring(0, 2))) {
            availability.addNearMatch(ShellGasStationFinder.mkService(record))
          } else {
            availability
          }
        }
      }
    }
    finally {
      reader.close()
    }
  }
}
