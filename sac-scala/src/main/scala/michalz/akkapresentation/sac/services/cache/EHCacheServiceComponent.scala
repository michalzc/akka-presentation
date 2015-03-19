package michalz.akkapresentation.sac.services.cache

import net.sf.ehcache.{CacheManager, Element}
import org.slf4j.LoggerFactory

/**
 * Created by michal on 19.03.15.
 */
trait EHCacheServiceComponent extends CacheServiceComponent {

  val logger = LoggerFactory.getLogger(classOf[EHCacheServiceComponent])

  def cacheName: String


  class EHCacheService extends CacheService {
    logger.info("Creating EHCacheService")
    private val cacheManager = CacheManager.newInstance()
    if(logger.isDebugEnabled) {
      logger.info("Existing caches: {}", cacheManager.getCacheNames.mkString(", "))
    }
    cacheManager.addCache(cacheName)
    logger.info("Cache added")

    def putToCache[K, V](key: K, value: V) = {
      cacheManager.getCache(cacheName).put(new Element(key, value))
    }

    def getFromCache[K, V](key: K) = {
      val element: Element = cacheManager.getCache(cacheName).get(key)
      if (element == null) None
      else Some(element.getObjectValue.asInstanceOf[V])
    }

    def shutdownCache = {
      cacheManager.shutdown()
    }
  }


  def cacheService = new EHCacheService
}
