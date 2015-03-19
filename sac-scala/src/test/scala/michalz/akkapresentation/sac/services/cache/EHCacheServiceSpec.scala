package michalz.akkapresentation.sac.services.cache

import michalz.akkapresentation.sac.domain.{ServiceNotFound, SearchInProgress, ServiceAvailability}
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

/**
 * Created by michal on 19.03.15.
 */
class EHCacheServiceSpec extends Specification with AfterAll {

  type CacheKey = (String, String)
  val ehCacheComponent = new EHCacheServiceComponent {}
  val cacheService = ehCacheComponent.cacheService("ehCacheServiceSpec")

  def afterAll = {
    cacheService.shutdownCache
  }

  "this is specification for ehcache service" >> {
    "it should put three values to cache and retrieve" >> {

      val key1 = ("1001", "51-122")
      val key2 = ("1001", "33-220")
      val key3 = ("2001", "33-152")

      val value1 = new ServiceAvailability(key1._2, key1._1, List(), List())
      val value2 = SearchInProgress
      val value3 = ServiceNotFound

      cacheService.putToCache(key1, value1)
      cacheService.putToCache(key2, value2)
      cacheService.putToCache(key3, value3)


      cacheService.getFromCache(key1) must beSome(value1)
      cacheService.getFromCache(key2) must beSome(value2)
      cacheService.getFromCache(key3) must beSome(value3)

    }
  }

}
