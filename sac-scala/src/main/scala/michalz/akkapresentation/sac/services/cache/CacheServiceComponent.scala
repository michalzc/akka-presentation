package michalz.akkapresentation.sac.services.cache

/**
 * Created by michal on 18.03.15.
 */
trait CacheServiceComponent {

  trait CacheService {
    def putToCache[K, V](key: K, value: V)
    def getFromCache[K, V](key: K): Option[V]
    def shutdownCache
  }

  def cacheService(cacheName: String): CacheService
}
